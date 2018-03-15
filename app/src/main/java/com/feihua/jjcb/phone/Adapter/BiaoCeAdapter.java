package com.feihua.jjcb.phone.Adapter;

import android.content.Context;
import android.widget.AbsListView;
import android.widget.LinearLayout;

import com.feihua.jjcb.phone.R;
import com.feihua.jjcb.phone.callback.BiaoCeTables;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.CategorySeries;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;

import java.util.List;

/**
 * Created by wcj on 2016-03-31.
 */
public class BiaoCeAdapter extends CommonAdapter<BiaoCeTables>
{
    private Context ctx;
    private DefaultRenderer renderer;

    public BiaoCeAdapter(Context context, List<BiaoCeTables> datas, int layoutId)
    {
        super(context, datas, layoutId);
        ctx = context;
        int[] colors = {context.getResources().getColor(R.color.circular2),context.getResources().getColor(R.color.circular3), context.getResources().getColor(R.color.circular1)};
        renderer = buildCategoryRenderer(colors);
    }

    @Override
    public void convert(ViewHolder holder, BiaoCeTables biaoCeTables, int position)
    {
        holder.setText(R.id.tv_item_cehao,biaoCeTables.getVOLUME_NO());
        holder.setText(R.id.tv_item_state,"已上传数:" + biaoCeTables.getVOLUME_UPDATA());
        holder.setText(R.id.tv_item_total,"总表数:" + biaoCeTables.getVOLUME_MCOUNT());
        holder.setText(R.id.tv_item_yichao,"已抄表数:" + biaoCeTables.getVOLUME_SAVE());

        //50抄表数542总数
        double[] values = {Double.valueOf(biaoCeTables.getVOLUME_UPDATA()),Double.valueOf(biaoCeTables.getVOLUME_SAVE()) -Double.valueOf(biaoCeTables.getVOLUME_UPDATA()), Double.valueOf(biaoCeTables.getVOLUME_MCOUNT()) - Double.valueOf(biaoCeTables.getVOLUME_SAVE())};
        CategorySeries dataset = buildCategoryDataset("", values);
        GraphicalView graphicalView = ChartFactory.getPieChartView(ctx, dataset, renderer);//饼状图
        LinearLayout item_ll_pie = holder.getView(R.id.item_ll_pie);
        item_ll_pie.removeAllViews();
        item_ll_pie.addView(graphicalView, new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.MATCH_PARENT));
    }

    protected CategorySeries buildCategoryDataset(String title, double[] values)
    {
        CategorySeries series = new CategorySeries(title);
        series.add(values[0]);
        series.add(values[1]);
        series.add(values[2]);
        return series;
    }

    protected DefaultRenderer buildCategoryRenderer(int[] colors)
    {
        DefaultRenderer renderer = new DefaultRenderer();
        renderer.setFitLegend(false);
        renderer.setStartAngle(-90);
        renderer.setShowLegend(false);//左下角标注
        renderer.setShowAxes(false);
        renderer.setShowLabels(false);
        //        renderer.setLegendTextSize(20);//设置左下角表注的文字大小
        //renderer.setZoomButtonsVisible(true);//设置显示放大缩小按钮
        renderer.setZoomEnabled(false);//设置不允许放大缩小.
        //        renderer.setChartTitleTextSize(30);//设置图表标题的文字大小
        //        renderer.setChartTitle("统计结果");//设置图表的标题  默认是居中顶部显示
        //        renderer.setLabelsTextSize(20);//饼图上标记文字的字体大小
        //        renderer.setLabelsColor(Color.BLACK);//饼图上标记文字的颜色
        renderer.setPanEnabled(false);//设置是否可以平移
        renderer.setDisplayValues(false);//是否显示值
        renderer.setClickEnabled(false);//设置是否可以被点击
        renderer.setMargins(new int[]{0, 0, 0, 0});
        //margins - an array containing the margin size values, in this order: top, left, bottom, right
        for (int color : colors)
        {
            SimpleSeriesRenderer r = new SimpleSeriesRenderer();
            r.setColor(color);
            renderer.addSeriesRenderer(r);
        }
        return renderer;
    }
}
