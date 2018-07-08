package android.smart.home.smarthome.util;



import android.content.Context;
import android.util.Log;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.util.List;


/**
 * Created by Administrator on 2017/11/6.
 *
 */

public class ChartUtils {

    /**
     * 初始化LineChar
     */
    public static void initLineChart(LineChart lineChart,String str,int descriptionColor,Context context) {
        lineChart.setDrawGridBackground(false);
        lineChart.setDrawBorders(false);
        //折线图例 标签 设置
        Legend legend = lineChart.getLegend();
        legend.setForm(Legend.LegendForm.LINE);
        legend.setTextSize(12f);
        //显示位置
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        legend.setDrawInside(false);
        //setDescription
        Description description = new Description();
        description.setText(str);
        description.setTextSize(8f);
        description.setTextColor(descriptionColor);
        lineChart.setDescription(description);
        //X轴设置显示位置在底部
        XAxis xAxis=lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        //保证Y轴从0开始，不然会上移一点
        YAxis leftAxis=lineChart.getAxisLeft();
        YAxis rightAxis=lineChart.getAxisRight();
        leftAxis.setAxisMinimum(0f);
        rightAxis.setAxisMinimum(0f);
        lineChart.setVisibleXRangeMinimum(8);
        lineChart.setVisibleXRangeMaximum(3);
        //添加一个空的 LineData
        LineData lineData = new LineData();
        lineChart.setData(lineData);
        lineChart.invalidate();
    }

    public static void setLineDataSetStyle(LineDataSet lineDataSet, int color,YAxis.AxisDependency dependency) {
            lineDataSet.setLineWidth(2f);
            lineDataSet.setCircleRadius(2f);
            lineDataSet.setColor(color);
            lineDataSet.setCircleColor(color);
            lineDataSet.setHighLightColor(color);
            lineDataSet.setDrawValues(true);
            //设置曲线填充
            lineDataSet.setDrawFilled(true);
            lineDataSet.setAxisDependency(dependency);
            lineDataSet.setValueTextSize(10f);
            lineDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
    }

    public static void addLineDataSet(LineChart lineChart, List<LineDataSet> lineDataSets){
        LineData data=lineChart.getData();
        if(data!=null) {
            for (LineDataSet lineDataSet:lineDataSets) {
                data.addDataSet(lineDataSet);
                Log.d("ceshi", "add one dataSet");
            }
            data.notifyDataChanged();
            lineChart.notifyDataSetChanged();
            lineChart.animateY(2000, Easing.EasingOption.EaseInOutSine);
            lineChart.animateX(1000, Easing.EasingOption.EaseInOutSine);
        }
    }

    public static void removeLineDataSet(LineChart lineChart){
        LineData data=lineChart.getData();
        while(data.getDataSetCount()>=1){
            data.removeDataSet(data.getDataSetByIndex(data.getDataSetCount()-1));
        }
        Log.d("ceshi","remove all");
        lineChart.notifyDataSetChanged();
        lineChart.invalidate();
    }

    /**
     * 设置Y轴值
     *
     * @param max
     * @param min
     * @param labelCount
     */
    public static void setYAxis(LineChart lineChart,float max, float min, int labelCount) {
        if (max < min) {
            return;
        }
        YAxis leftAxis=lineChart.getAxisLeft();
        leftAxis.setAxisMaximum(max);
        leftAxis.setAxisMinimum(min);
        leftAxis.setLabelCount(labelCount, false);
        YAxis rightAxis=lineChart.getAxisRight();
        rightAxis.setAxisMaximum(90);
        rightAxis.setAxisMinimum(0);
        rightAxis.setLabelCount(10, false);
        lineChart.invalidate();
    }

    /**
     * 设置X轴值
     */
    public static void setXAxis(LineChart lineChart,IAxisValueFormatter xFormatter) {
        XAxis xAxis=lineChart.getXAxis();
        xAxis.setValueFormatter(xFormatter);
        lineChart.invalidate();
    }

    /**
     * 设置高限制线
     *
     * @param high
     * @param name
     */
    public static void setHightLimitLine(LineChart lineChart,float high, String name, int color) {
        if (name == null) {
            name = "高限制线";
        }
        LimitLine hightLimit = new LimitLine(high, name);
        hightLimit.setLineWidth(1f);
        hightLimit.setTextSize(8f);
        hightLimit.setLineColor(color);
        hightLimit.setTextColor(color);
        YAxis leftAxis=lineChart.getAxisLeft();
        leftAxis.addLimitLine(hightLimit);
        lineChart.invalidate();
    }

    /**
     * 设置低限制线
     *
     * @param low
     * @param name
     */
    public static void setLowLimitLine(LineChart lineChart,int low, String name,int color) {
        if (name == null) {
            name = "低限制线";
        }
        LimitLine lowLimit = new LimitLine(low, name);
        lowLimit.setLineWidth(1f);
        lowLimit.setLineColor(color);
        lowLimit.setTextSize(8f);
        lowLimit.setTextColor(color);
        YAxis leftAxis=lineChart.getAxisLeft();
        leftAxis.addLimitLine(lowLimit);
        lineChart.invalidate();
    }

}
