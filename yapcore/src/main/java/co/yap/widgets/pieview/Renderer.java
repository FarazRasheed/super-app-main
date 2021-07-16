
package co.yap.widgets.pieview;


/**
 * Abstract baseclass of all Renderers.
 *
 * @author Mirza Adil
 */
public abstract class Renderer {

    /**
     * the component that handles the drawing area of the chart and it's offsets
     */
    protected ViewPortHandler mViewPortHandler;

    public Renderer(ViewPortHandler viewPortHandler) {
        this.mViewPortHandler = viewPortHandler;
    }
}
