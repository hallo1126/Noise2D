package me.dags.noise;

import me.dags.noise.source.Constant;

/**
 * @author dags <dags@dags.me>
 *
 * Source modules are expected to return values between -1.0 and 1.0
 */
public interface Source extends Module {

    Builder toBuilder();

    default float minValue() {
        return -1F;
    }

    default float maxValue() {
        return 1F;
    }

    /**
     * @return A noise source builder
     */
    static Builder builder() {
        return new Builder();
    }

    /**
     * @return A Source Module with the given seed, scale and octaves
     */
    static Source perlin(int seed, int scale, int octaves) {
        return perlin(seed, 1F / scale, octaves);
    }

    /**
     * @return A Source Module with the given seed, frequency and octaves
     */
    static Source perlin(int seed, double frequency, int octaves) {
        return builder().seed(seed).frequency(frequency).octaves(octaves).perlin();
    }

    /**
     * @return A Source Module with the given seed, scale and octaves
     */
    static Source billow(int seed, int scale, int octaves) {
        return billow(seed, 1F / scale, octaves);
    }

    /**
     * @return A Source Module with the given seed, frequency and octaves
     */
    static Source billow(int seed, double frequency, int octaves) {
        return builder().seed(seed).frequency(frequency).octaves(octaves).billow();
    }

    /**
     * @return A Source Module with the given seed, scale and octaves
     */
    static Source ridge(int seed, int scale, int octaves) {
        return ridge(seed, 1F / scale, octaves);
    }

    /**
     * @return A Source Module with the given seed, frequency and octaves
     */
    static Source ridge(int seed, double frequency, int octaves) {
        return builder().seed(seed).frequency(frequency).octaves(octaves).ridge();
    }

    /**
     * @return A Source Module with the given seed, scale and octaves
     */
    static Source cubic(int seed, int scale, int octaves) {
        return cubic(seed, 1F / scale, octaves);
    }

    /**
     * @return A Source Module with the given seed, frequency and octaves
     */
    static Source cubic(int seed, double frequency, int octaves) {
        return builder().seed(seed).frequency(frequency).octaves(octaves).cubic();
    }

    static Source cell(int seed, int scale) {
        return cell(seed, 1F / scale);
    }

    static Source cell(int seed, double frequency) {
        return builder().seed(seed).frequency(frequency).cell();
    }

    /**
     * @return A Source Module with the given seed, scale and octaves
     */
    static Source cellEdge(int seed, int scale) {
        return cellEdge(seed, 1F / scale);
    }

    /**
     * @return A Source Module with the given seed, frequency and octaves
     */
    static Source cellEdge(int seed, double frequency) {
        return builder().seed(seed).frequency(frequency).cellEdge();
    }

    /**
     * @return A module with a constant output value
     */
    static Source constant(double value) {
        return new Constant((float) value);
    }
}
