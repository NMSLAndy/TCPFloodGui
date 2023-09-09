package xyz.NetMelody.Utils.Animating;

public class LinearTranslator
{
    private float start;
    private long lastMS;
    
    public LinearTranslator(int start) {
        super();
        this.start = start;
        this.lastMS = System.currentTimeMillis();
    }
    
    public void interpolate(float target) {
        long currentMS = System.currentTimeMillis();
        long delta = currentMS - this.lastMS;
        this.lastMS = currentMS;
        this.start = AnimationUtil.calculateCompensation(target, this.start, delta, 20);
    }
    
    public void interp(float target, float f) {
        long currentMS = System.currentTimeMillis();
        long delta = currentMS - this.lastMS;
        this.lastMS = currentMS;
        this.start = AnimationUtil.calculateCompensation(target, this.start, delta, f);
    }
    
    public float getCurrent() {
        return this.start;
    }
    
    public void setCurrent(float opacity) {
        this.start = opacity;
    }
}
