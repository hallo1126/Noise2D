package me.dags.noise.combiner;

import me.dags.config.Node;
import me.dags.noise.Module;
import me.dags.noise.func.Interpolation;
import me.dags.noise.util.Util;

/**
 * @author dags <dags@dags.me>
 */
public class Select extends Combiner {

    public static final Interpolation INTERPOLATION = Interpolation.HERMITE;

    private final Module control;
    private final Module source0;
    private final Module source1;
    private final Interpolation interpolation;

    private final float lowerBound;
    private final float upperBound;
    private final float edgeFalloff;
    private final float lowerCurve0;
    private final float upperCurve0;
    private final float lowerCurve1;
    private final float upperCurve1;
    private final float lowerBoundMin;
    private final float lowerBoundMax;
    private final float upperBoundMin;
    private final float upperBoundMax;

    public Select(Module control, Module source0, Module source1, float lowerBound, float upperBound, float edgeFalloff, Interpolation interpolation) {
        super(source0, source1);
        this.control = control;
        this.source0 = source0;
        this.source1 = source1;
        this.interpolation = interpolation;
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
        this.edgeFalloff = edgeFalloff;
        this.lowerCurve0 = lowerBound - edgeFalloff;
        this.upperCurve0 = lowerBound + edgeFalloff;
        this.lowerCurve1 = upperBound - edgeFalloff;
        this.upperCurve1 = upperBound + edgeFalloff;
        this.lowerBoundMin = lowerBound - edgeFalloff;
        this.lowerBoundMax = lowerBound + edgeFalloff;
        this.upperBoundMin = upperBound - edgeFalloff;
        this.upperBoundMax = upperBound + edgeFalloff;
    }

    @Override
    public void toNode(Node node) {
        super.toNode(node);
        control.toNode(node.node("selector"));
        node.set("boundLower", Util.round5(lowerBound));
        node.set("boundUpper", Util.round5(upperBound));
        node.set("falloff", Util.round5(edgeFalloff));
    }

    @Override
    public String getName() {
        return "select";
    }

    @Override
    protected float minTotal(float result, Module next) {
        return Math.min(result, next.minValue());
    }

    @Override
    protected float maxTotal(float result, Module next) {
        return Math.max(result, next.maxValue());
    }

    @Override
    protected float combine(float result, float value) {
        return 0F;
    }

    @Override
    public float getValue(float x, float y) {
        float controlValue = control.getValue(x, y);
        if (edgeFalloff > 0.0) {
            if (controlValue < lowerBoundMin) {
                return source0.getValue(x, y);
            } else if (controlValue < lowerBoundMax) {
                float alpha = curve((controlValue - lowerCurve0) / (upperCurve0 - lowerCurve0));
                return linearInterp(source0.getValue(x, y), source1.getValue(x, y), alpha);
            } else if (controlValue < upperBoundMin) {
                return source1.getValue(x, y);
            } else if (controlValue < upperBoundMax) {
                float alpha = curve((controlValue - lowerCurve1) / (upperCurve1 - lowerCurve1));
                return linearInterp(source1.getValue(x, y), source0.getValue(x, y), alpha);
            } else {
                return source0.getValue(x, y);
            }
        } else if (controlValue < lowerBound || controlValue > upperBound) {
            return source0.getValue(x, y);
        } else {
            return source1.getValue(x, y);
        }
    }

    @Override
    public String toString() {
        return getName() + "{"
                + "selector=" + control
                + ",source0=" + source0
                + ",source1=" + source1
                + "}";
    }

    private float curve(float a) {
        return interpolation.apply(a);
    }

    private static float linearInterp(float n0, float n1, float a) {
        return ((1.0F - a) * n0) + (a * n1);
    }
}
