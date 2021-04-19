package com.github.loyada.jdollarx.singlebrowser;

import java.util.concurrent.TimeUnit;


/**
 * An Autocloaseable that allows to temporarily change the implicit timeout
 */
public class TemporaryChangedTimeout  implements AutoCloseable {
    private final int savedTimeout;
    private final TimeUnit savedUnit;

    /**
     * Temporarily override the implicit timeout of Selenium. Useful when you perform "smart"
     * operations that you know might not find the elements, and you want to speed it up.
     * @param implicitTimeout - implicit timeout for Selenium
     * @param unit unit of the timeout
     */
    public TemporaryChangedTimeout(int implicitTimeout, TimeUnit unit) {
        this.savedTimeout = InBrowserSinglton.getImplicitTimeout();
        this.savedUnit = InBrowserSinglton.getTimeoutUnit();
        InBrowserSinglton.setImplicitTimeout(implicitTimeout, unit);
    }


    @Override
    public void close() throws Exception {
        InBrowserSinglton.setImplicitTimeout(this.savedTimeout, this.savedUnit);
    }
}
