package com.example.qiubo.goaltracker.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by LSY on 2017/9/17.
 * 用于屏蔽viewpager的左右滑动和切换时的滚动动画
 *
 * 禁止ViewPager左右滑动:
 * 需重写ViewPager，覆盖ViewPager的onInterceptTouchEvent(MotionEvent arg0)方法
 * 和onTouchEvent(MotionEvent arg0)方法
 * 这两个方法的返回值都是boolean类型的，只需要将返回值改为false
 * 这样ViewPager就不会消耗掉手指滑动的事件了，转而传递给上层View去处理或者该事件就直接终止了。
 *
 * 静止viewpager切换动画：
 * 则需重写viewpager的setCurrentItem()方法就可以了
 */
public class NoScrollViewPager extends ViewPager {

    private boolean isCanScroll = true;
    private boolean isHasScrollAnim=true;

    public NoScrollViewPager(Context context) {
        super(context);
    }

    public NoScrollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 设置其是否能滑动
     * @param isCanScroll false 禁止滑动， true 可以滑动
     */
    public void setScanScroll(boolean isCanScroll) {
        this.isCanScroll = isCanScroll;
    }

    public void setHasScrollAnim(boolean isHasScrollAnim){
        this.isHasScrollAnim=isHasScrollAnim;
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return isCanScroll && super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return isCanScroll && super.onTouchEvent(ev);
    }

    @Override
    public void setCurrentItem(int item, boolean smoothScroll) {
        super.setCurrentItem(item, smoothScroll);
    }

    /**
     * 设置其是否去求切换时的滚动动画
     *isHasScrollAnim为false时，会去除滚动效果
     */
    @Override
    public void setCurrentItem(int item) {
        super.setCurrentItem(item,isHasScrollAnim);
    }

}
