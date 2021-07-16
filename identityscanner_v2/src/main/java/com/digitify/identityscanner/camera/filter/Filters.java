package com.digitify.identityscanner.camera.filter;

import androidx.annotation.NonNull;

import com.digitify.identityscanner.camera.filters.AutoFixFilter;
import com.digitify.identityscanner.camera.filters.BlackAndWhiteFilter;
import com.digitify.identityscanner.camera.filters.BrightnessFilter;
import com.digitify.identityscanner.camera.filters.ContrastFilter;
import com.digitify.identityscanner.camera.filters.CrossProcessFilter;
import com.digitify.identityscanner.camera.filters.DocumentaryFilter;
import com.digitify.identityscanner.camera.filters.DuotoneFilter;
import com.digitify.identityscanner.camera.filters.FillLightFilter;
import com.digitify.identityscanner.camera.filters.GammaFilter;
import com.digitify.identityscanner.camera.filters.GrainFilter;
import com.digitify.identityscanner.camera.filters.GrayscaleFilter;
import com.digitify.identityscanner.camera.filters.HueFilter;
import com.digitify.identityscanner.camera.filters.InvertColorsFilter;
import com.digitify.identityscanner.camera.filters.LomoishFilter;
import com.digitify.identityscanner.camera.filters.PosterizeFilter;
import com.digitify.identityscanner.camera.filters.SaturationFilter;
import com.digitify.identityscanner.camera.filters.SepiaFilter;
import com.digitify.identityscanner.camera.filters.SharpnessFilter;
import com.digitify.identityscanner.camera.filters.TemperatureFilter;
import com.digitify.identityscanner.camera.filters.TintFilter;
import com.digitify.identityscanner.camera.filters.VignetteFilter;

/**
 * Contains commonly used {@link Filter}s.
 *
 * You can use {@link #newInstance()} to create a new instance and
 * pass it to {@link com.digitify.identityscanner.camera.CameraView#setFilter(Filter)}.
 */
public enum Filters {

    /** @see NoFilter */
    NONE(NoFilter.class),

    /** @see AutoFixFilter */
    AUTO_FIX(AutoFixFilter.class),

    /** @see BlackAndWhiteFilter */
    BLACK_AND_WHITE(BlackAndWhiteFilter.class),

    /** @see BrightnessFilter */
    BRIGHTNESS(BrightnessFilter.class),

    /** @see ContrastFilter */
    CONTRAST(ContrastFilter.class),

    /** @see CrossProcessFilter */
    CROSS_PROCESS(CrossProcessFilter.class),

    /** @see DocumentaryFilter */
    DOCUMENTARY(DocumentaryFilter.class),

    /** @see DuotoneFilter */
    DUOTONE(DuotoneFilter.class),

    /** @see FillLightFilter */
    FILL_LIGHT(FillLightFilter.class),

    /** @see GammaFilter */
    GAMMA(GammaFilter.class),

    /** @see GrainFilter */
    GRAIN(GrainFilter.class),

    /** @see GrayscaleFilter */
    GRAYSCALE(GrayscaleFilter.class),

    /** @see HueFilter */
    HUE(HueFilter.class),

    /** @see InvertColorsFilter */
    INVERT_COLORS(InvertColorsFilter.class),

    /** @see LomoishFilter */
    LOMOISH(LomoishFilter.class),

    /** @see PosterizeFilter */
    POSTERIZE(PosterizeFilter.class),

    /** @see SaturationFilter */
    SATURATION(SaturationFilter.class),

    /** @see SepiaFilter */
    SEPIA(SepiaFilter.class),

    /** @see SharpnessFilter */
    SHARPNESS(SharpnessFilter.class),

    /** @see TemperatureFilter */
    TEMPERATURE(TemperatureFilter.class),

    /** @see TintFilter */
    TINT(TintFilter.class),

    /** @see VignetteFilter */
    VIGNETTE(VignetteFilter.class);

    private Class<? extends Filter> filterClass;

    Filters(@NonNull Class<? extends Filter> filterClass) {
        this.filterClass = filterClass;
    }

    /**
     * Returns a new instance of the given filter.
     * @return a new instance
     */
    @NonNull
    public Filter newInstance() {
        try {
            return filterClass.newInstance();
        } catch (IllegalAccessException e) {
            return new NoFilter();
        } catch (InstantiationException e) {
            return new NoFilter();
        }
    }
}
