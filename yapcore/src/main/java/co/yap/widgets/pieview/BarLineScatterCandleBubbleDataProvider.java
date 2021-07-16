package co.yap.widgets.pieview;


public interface BarLineScatterCandleBubbleDataProvider extends ChartInterface {


    Transformer getTransformer(YAxis.AxisDependency axis);

    boolean isInverted(YAxis.AxisDependency axis);

    float getLowestVisibleX();

    float getHighestVisibleX();

    BarLineScatterCandleBubbleData getData();
}
