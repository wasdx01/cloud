package com.bochao.project.model.service;

public class UgxBuildCacheParams {
    
    private float[] floatLod;
    private float[] floatBoxSize;
    private float length;
    private float width;
    private int nMaxLodLevel;
    private int nMaxTreeDepth;
    private float nRootVolumeFactor;

    public float[] getFloatLod() {
        return floatLod;
    }

    public void setFloatLod(float[] floatLod) {
        this.floatLod = floatLod;
    }

    public float[] getFloatBoxSize() {
        return floatBoxSize;
    }

    public void setFloatBoxSize(float[] floatBoxSize) {
        this.floatBoxSize = floatBoxSize;
    }

    public float getLength() {
        return length;
    }

    public void setLength(float length) {
        this.length = length;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public int getnMaxLodLevel() {
        return nMaxLodLevel;
    }

    public void setnMaxLodLevel(int nMaxLodLevel) {
        this.nMaxLodLevel = nMaxLodLevel;
    }

    public int getnMaxTreeDepth() {
        return nMaxTreeDepth;
    }

    public void setnMaxTreeDepth(int nMaxTreeDepth) {
        this.nMaxTreeDepth = nMaxTreeDepth;
    }

    public float getnRootVolumeFactor() {
        return nRootVolumeFactor;
    }

    public void setnRootVolumeFactor(float nRootVolumeFactor) {
        this.nRootVolumeFactor = nRootVolumeFactor;
    }
}
