package android.smart.home.smarthome.fragment;


import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.smart.home.smarthome.Interface.QueryDataPointsList;
import android.smart.home.smarthome.R;
import android.smart.home.smarthome.entity.OnenetDataPoints;
import android.smart.home.smarthome.util.ChartUtils;
import android.smart.home.smarthome.util.HttpUtils;
import android.smart.home.smarthome.widget.LineChartInViewPager;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.TimePicker;

import com.allen.library.SuperButton;
import com.allen.library.SuperTextView;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by Administrator on 2017/11/4.
 *
 */

public class HomeFragment extends BaseFragment implements SuperTextView.OnSwitchCheckedChangeListener,
        DatePickerDialog.OnDateSetListener,TimePickerDialog.OnTimeSetListener{
    private SuperTextView stv_showTemperature;
    private SuperTextView stv_getData;
    private SuperButton sbt_query;
    private LineChartInViewPager lineChart;
    private Calendar calendar;
    private Gson gson;
    private DatePickerDialog mDatePickerDialog;
    private TimePickerDialog mTimePickerDialog;
    private SimpleDateFormat simpleDateFormat;
    private IAxisValueFormatter xFormatter;//数据表X轴的格式器
    private QueryDataPointsList queryDataPointsList;//retrofit接口
    //private HistoryDataInterface historyDataInterface;//bigIot接口
    private static final String dht11_device_id="29670406";
    private static final String dataStreamID="dht11_temp,dht11_humi";
    //private static final String ERROR_TOKEN = "error_token";
    //private static final String ERROR_RETRY = "error_retry";
    private static final String TAG = "ceshi";
    private String startTime,endTime,lastDate;
    //private String cursor;//静态查询时返回的游标
    private List<String> timeList = new ArrayList<>(); //存储x轴的时间
    private CompositeDisposable compositeDisposable;//心跳任务管理器
    private boolean isDataEmpty;//判断获取的数据集是否为空，空则设为true
    private int successiveEmptyDataCount=0;//连续三次获取数据为空自动关闭动态刷新功能
    private volatile boolean isStart,isEnd,haveCursor,haveStart=false,haveEnd=false,isChecked=true;
    //设置开始时间；设置结束时间；是否返回游标；是否已设置开始时间；是否已设置结束时间；是否开启动态刷新功能


    @Override
    protected void initEvents() {
        simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        calendar=Calendar.getInstance();
        gson = new Gson();
        queryDataPointsList = HttpUtils.getInstance().getDataPoints();
        //historyDataInterface= HttpUtils.getInstance().getHistoryDataInterface();
        compositeDisposable =new CompositeDisposable();
        //startRequestData();
        createIntervalTask(30, TimeUnit.SECONDS);//心跳频率30秒
        stv_getData.setCenterTopTvClickListener(new SuperTextView.OnCenterTopTvClickListener() {
            @Override
            public void onClickListener() {
                if (isChecked){
                    ShowToast("请关闭动态刷新功能");
                }else {
                    setQueryDate();
                    isStart = true;
                }
            }
        }).setCenterBottomTvClickListener(new SuperTextView.OnCenterBottomTvClickListener() {
            @Override
            public void onClickListener() {
                if (isChecked){
                    ShowToast("请关闭动态刷新功能");
                }else {
                    setQueryDate();
                    isEnd = true;
                }
            }
        }).setSwitchCheckedChangeListener(this);

        sbt_query.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!haveStart||!haveEnd){
                    ShowToast("请选择查询区间");
                }else{
                    if(isChecked){
                            ShowToast("请关闭动态刷新功能");
                    }else {
                        Log.d(TAG,"staticquery");
                        staticQuery();
                    }
                }
            }
        });
    }

    private void setQueryDate(){
        Calendar initCalendar=Calendar.getInstance();
        int initYear=initCalendar.get(Calendar.YEAR);
        int initMonth=initCalendar.get(Calendar.MONTH);
        final int initDay=initCalendar.get(Calendar.DAY_OF_MONTH);
        mDatePickerDialog=new DatePickerDialog(context,this,initYear,initMonth,initDay);
        mDatePickerDialog.setTitle("请选择日期");
        mDatePickerDialog.setCanceledOnTouchOutside(false);
        mDatePickerDialog.setButton(DialogInterface.BUTTON_POSITIVE, "设置", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DatePicker mDatePicker=mDatePickerDialog.getDatePicker();
                mDatePicker.clearFocus();
                onDateSet(mDatePicker,mDatePicker.getYear(),mDatePicker.getMonth(),mDatePicker.getDayOfMonth());
                mDatePickerDialog.cancel();
                Calendar initCalendar=Calendar.getInstance();
                int initHour=initCalendar.get(Calendar.HOUR_OF_DAY);
                int initMinute=initCalendar.get(Calendar.MINUTE);
                mTimePickerDialog=new TimePickerDialog(context,HomeFragment.this,initHour,initMinute,true);
                mTimePickerDialog.setCanceledOnTouchOutside(false);
                mTimePickerDialog.setTitle("请选择时间");
                mTimePickerDialog.show();
            }
        });
        mDatePickerDialog.show();
    }

    private void staticQuery(){
        startTime=startTime.replace(" ","T");
        endTime=endTime.replace(" ","T");
        Observable<OnenetDataPoints> staticRequest = queryDataPointsList.
                getDataPointsList(dht11_device_id,dataStreamID,startTime,
                endTime,null,20,null);
        requestDataToRefreshUI(staticRequest);
    }

    /**
     * 数据转换
     * @param oneNetResponse  数据响应，Gson对象
     * @return  折线数据集列表
     */

    private List<LineDataSet> DataTransform(OnenetDataPoints oneNetResponse){
        String response = gson.toJson(oneNetResponse);
        Log.d("ceshi",response);
        List<LineDataSet> lineDataSets = new ArrayList<>();
        int count=oneNetResponse.getData().getCount();
        if(count==0){
            isDataEmpty=true;
            lineDataSets.add(new LineDataSet(null,"温度"));
            lineDataSets.add(new LineDataSet(null,"湿度"));
            return lineDataSets;
        }
        isDataEmpty=false;
        List<OnenetDataPoints.DataBean.DatastreamsBean.DatapointsBean> tempDataPoints=oneNetResponse.getData().getDatastreams().get(0).getDatapoints();
        List<OnenetDataPoints.DataBean.DatastreamsBean.DatapointsBean> humiDataPoints=oneNetResponse.getData().getDatastreams().get(1).getDatapoints();
        List<Entry> tempInfo = new ArrayList<>();
        List<Entry> humiInfo = new ArrayList<>();
        timeList.clear();
        for (int i = 0; i < tempDataPoints.size(); i++) {
            String date = tempDataPoints.get(i).getAt();
            String time = date.substring(11, 19);
            Float temp = tempDataPoints.get(i).getValue();
            Float humi = humiDataPoints.get(i).getValue();
            tempInfo.add(new Entry(i, temp));
            humiInfo.add(new Entry(i, humi));
            timeList.add(time);
        }
        lastDate=tempDataPoints.get(tempDataPoints.size()-1).getAt().substring(0,19);
        xFormatter = new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return timeList.get((int) value % timeList.size());
            }
        };
        lineDataSets.add(new LineDataSet(tempInfo, "温度"));
        lineDataSets.add(new LineDataSet(humiInfo, "湿度"));
        return lineDataSets;
    }

    /**
     * 刷新视图
     * @param lineDataSets  折线数据集
     */
    private void refreshView(List<LineDataSet> lineDataSets){
        if(isDataEmpty){
            successiveEmptyDataCount++;
            ShowToast("没有最新数据");
            if(successiveEmptyDataCount==3&&compositeDisposable.size()>0){
                compositeDisposable.clear();
                ShowToast("实时刷新已自动关闭");
            }
            return;
        }
        ChartUtils.removeLineDataSet(lineChart);
        Log.d(TAG, "afterRemove");
        int tempColor = getResources().getColor(R.color.light_blue);
        int humiColor = getResources().getColor(R.color.lineDataSetColor);
        ChartUtils.setLineDataSetStyle(lineDataSets.get(0),tempColor, YAxis.AxisDependency.LEFT);
        ChartUtils.setLineDataSetStyle(lineDataSets.get(1),humiColor, YAxis.AxisDependency.RIGHT);
        ChartUtils.addLineDataSet(lineChart, lineDataSets);
        Log.d(TAG, "successful add one dataSet");
        ChartUtils.setXAxis(lineChart, xFormatter);
        ChartUtils.setHightLimitLine(lineChart,36,"高温预警",getResources().getColor(R.color.red));
        ChartUtils.setLowLimitLine(lineChart,4,"低温预警",getResources().getColor(R.color.endcolor));
        Entry entry = lineDataSets.get(0).getValues().get(lineDataSets.get(0).getEntryCount()-1);
        Float temp=entry.getY();
        stv_showTemperature.setCenterBottomString(lastDate);
        stv_showTemperature.setCenterTopString(temp.toString()+" ℃");
        Log.d(TAG,"refresh view");
        successiveEmptyDataCount=0;
    }
/*
    /**
     * @param filed 延时的单位（例如：Calendar.MINUTE）
     * @param value 延时的具体值
     * @return 当前时间向前提早一定值后的字符串表示
     */

    private String getCurrentTimeAsStartTimeOfQuery(int filed,int value){
        Date start = new Date();
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(start);
        calendar.set(filed,calendar.get(filed)-value);
        start=calendar.getTime();
        String startTime=simpleDateFormat.format(start);
        startTime=startTime.replace(" ","T");
        return startTime;
    }

    /**
     * 心跳间隔尽量与请求中的延时间隔保持一致
     * @param period  心跳间隔
     * @param unit  时间单位
     */
    private void createIntervalTask(long period, TimeUnit unit){
        Disposable disposable = Observable.intervalRange(0,1000,0,period,unit,Schedulers.newThread())
                .doOnNext(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        Log.d("ceshi","doOnnext");
                        String dynamicStart=getCurrentTimeAsStartTimeOfQuery(Calendar.MINUTE,5);
                        Observable<OnenetDataPoints> dynamicRequest= queryDataPointsList.getDataPointsList(dht11_device_id,dataStreamID,
                                dynamicStart,null,300,null,null);//300秒提前量，即5分钟
                        requestDataToRefreshUI(dynamicRequest);
                    }
                }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {}
                });
        compositeDisposable.add(disposable);
    }

    private void requestDataToRefreshUI(Observable<OnenetDataPoints> request){
        request.subscribeOn(Schedulers.io()).doOnNext(new Consumer<OnenetDataPoints>() {
            @Override
            public void accept(OnenetDataPoints oneNetResponse) throws Exception {
                if(oneNetResponse.getData().getCursor()!=null){
                    haveCursor=true;
                    //cursor=oneNetResponse.getData().getCursor();
                }else{
                    haveCursor=false;
                }
            }
        }).map(new Function<OnenetDataPoints,List<LineDataSet>>() {
            @Override
            public List<LineDataSet> apply(OnenetDataPoints oneNetResponse) throws Exception {
                return DataTransform(oneNetResponse);
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<LineDataSet>>() {
                    @Override
                    public void accept(List<LineDataSet> lineDataSets) throws Exception {
                        Log.d(TAG,"consumer");
                        refreshView(lineDataSets);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.d(TAG,"request failed");
                        Log.d(TAG,throwable.toString());
                        Log.d(TAG,throwable.getMessage());
                    }
                });

    }


    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        isChecked=b;
        if(b){
            if(compositeDisposable.size()==0)
                createIntervalTask(1,TimeUnit.MINUTES);
                Log.d("ceshi","createIntervalTask");
        }else{
            if(compositeDisposable.size()>0)
                compositeDisposable.clear();
            Log.d("ceshi","clear");
        }
    }


    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        calendar.set(year,month,dayOfMonth);
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        calendar.set(Calendar.HOUR_OF_DAY,hourOfDay);
        calendar.set(Calendar.MINUTE,minute);
        calendar.set(Calendar.SECOND,0);
        if (isStart){
            Date start=calendar.getTime();
            startTime=simpleDateFormat.format(start);
            stv_getData.setCenterTopString(startTime);
            stv_getData.setCenterTopTextColor(R.color.base_color_text_black);
            haveStart=true;
            isStart=false;
        }else if(isEnd){
            Date end=calendar.getTime();
            endTime=simpleDateFormat.format(end);
            stv_getData.setCenterBottomString(endTime);
            stv_getData.setCenterBottomTextColor(R.color.base_color_text_black);
            haveEnd=true;
            isEnd=false;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(TAG,"destroy");
        successiveEmptyDataCount=0;
        if(compositeDisposable!=null&&compositeDisposable.size()>0){
            compositeDisposable.clear();
            Log.d(TAG,"clear");
        }
    }

    @Override
    protected int getResId() {
        return R.layout.fragment_home_page;
    }

    @Override
    protected void onRealViewLoaded(View view) {
        stv_showTemperature = (SuperTextView) view.findViewById(R.id.stv_show_temp);
        stv_getData = (SuperTextView) view.findViewById(R.id.stv_get_data);
        sbt_query = (SuperButton) view.findViewById(R.id.sbt_query);
        lineChart=(LineChartInViewPager) view.findViewById(R.id.chart);
        ChartUtils.initLineChart(lineChart,"室内气温",getResources().getColor(R.color.endcolor),getActivity());
        ChartUtils.setYAxis(lineChart,40,0,5);
        initEvents();
    }
/*
    protected void startRequestData(){
        Observable<List<HistoryDataPoints>> observable = Observable.defer(new Callable<ObservableSource<String>>() {
            @Override
            public ObservableSource<String> call() throws Exception {
                TokenLoader.setContext(getActivity().getApplicationContext());
                TokenLoader instance = TokenLoader.getInstance();
                String cacheToken = instance.getCacheToken();
                Log.d(TAG,"获取到缓存Token=" + cacheToken);
                return Observable.just(cacheToken);
            }
        }).flatMap(new Function<String, ObservableSource<List<HistoryDataPoints>>>() {
            @Override
            public ObservableSource<List<HistoryDataPoints>> apply(String token) throws Exception {
                return getHistoryDataObservable(token);
            }
        }).retryWhen(new Function<Observable<Throwable>, ObservableSource<?>>() {
            private int mRetryCount = 0;
            @Override
            public ObservableSource<?> apply(Observable<Throwable> throwableObservable) throws Exception {
                return throwableObservable.flatMap(new Function<Throwable, ObservableSource<?>>() {
                    @Override
                    public ObservableSource<?> apply(Throwable throwable) throws Exception {
                        Log.d(TAG,"发生错误=" + throwable + ",重试次数=" + mRetryCount);
                        if (mRetryCount > 0) {
                            return Observable.error(new Throwable(ERROR_RETRY));
                        } else if (ERROR_TOKEN.equals(throwable.getMessage())) {
                            mRetryCount++;
                            return TokenLoader.getInstance().getNetTokenLocked();
                        } else {
                            return Observable.error(throwable);
                        }
                    }
                });
            }
        });
        DisposableObserver<List<HistoryDataPoints>> observer = new DisposableObserver<List<HistoryDataPoints>>() {

            @Override
            public void onNext(List<HistoryDataPoints> value) {
                refreshView(DataTransform(value));
            }
            @Override
            public void onError(Throwable e) {}
            @Override
            public void onComplete() {}
        };
        observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(observer);
    }

    private Observable<List<HistoryDataPoints>> getHistoryDataObservable(String token){
        return historyDataInterface.getHumidity(token, "3414");
    }

    private LineDataSet DataTransform(List<HistoryDataPoints> historyDataPoints){
        String response = gson.toJson(historyDataPoints);
        Log.d("ceshi",response);
        List<Entry> tempInfo = new ArrayList<>();
        timeList.clear();
        for (int i = 0; i < historyDataPoints.size(); i++) {
            String date = historyDataPoints.get(i).getTime();
            String timeX = simpleDateFormat.format(new Date(Long.valueOf(date)));
            Log.d(TAG,timeX);
            //String time = date.substring(11, 19);
            Float temp = Float.valueOf(historyDataPoints.get(i).getValue());
            tempInfo.add(new Entry(i, temp));
            timeList.add(timeX);
        }
        lastDate=historyDataPoints.get(historyDataPoints.size()-1).getTime();
        xFormatter = new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return timeList.get((int) value % timeList.size());
            }
        };
        return new LineDataSet(tempInfo, "湿度");
    }
*/
}
