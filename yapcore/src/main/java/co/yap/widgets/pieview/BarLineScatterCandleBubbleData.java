
package co.yap.widgets.pieview;

import java.util.List;

/**
 * Baseclass for all Line, Bar, Scatter, Candle and Bubble data.
 * 
 * @author Mirza Adil
 */
public abstract class BarLineScatterCandleBubbleData<T extends IBarLineScatterCandleBubbleDataSet<? extends Entry>>
        extends ChartData<T> {
    
    public BarLineScatterCandleBubbleData() {
        super();
    }

    public BarLineScatterCandleBubbleData(T... sets) {
        super(sets);
    }

    public BarLineScatterCandleBubbleData(List<T> sets) {
        super(sets);
    }
}
