package android.smart.home.smarthome.animation;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;

import com.ufreedom.floatingview.spring.SimpleReboundListener;
import com.ufreedom.floatingview.spring.SpringHelper;
import com.ufreedom.floatingview.transition.FloatingTransition;
import com.ufreedom.floatingview.transition.YumFloating;

/**
 * Created by Administrator on 2017/11/14.
 *
 */

public class ChangedScaleFloatingTransition implements FloatingTransition {

    private long duration;
    private double bounciness;
    private double speed;

    public ChangedScaleFloatingTransition(){
        duration=1000;
        bounciness=10;
        speed=15;
    }

    public ChangedScaleFloatingTransition(long duration){
        this.duration=duration;
        bounciness=10;
        speed=15;
    }

    public ChangedScaleFloatingTransition(long duration,double bounciness,double speed){
        this.duration=duration;
        this.bounciness=bounciness;
        this.speed=speed;
    }


    @Override
    public void applyFloating(final YumFloating yumFloating) {


        SpringHelper.createWithBouncinessAndSpeed(0.5f,1.0f,bounciness,speed)
                .reboundListener(new SimpleReboundListener(){
                    @Override
                    public void onReboundUpdate(double currentValue) {
                        yumFloating.setScaleX((float) currentValue);
                        yumFloating.setScaleY((float) currentValue);
                    }
                }).start(yumFloating);
    }
}
