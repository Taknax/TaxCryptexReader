package tax.taknax.taxcr.common.util;

@SuppressWarnings("unused")
public enum EnumBookDesign {

    VANILLA(0, 9, 1.0F),
    CONCEPT(1, 5, 0.5F);

    private final int id;
    private final int frames;
    private final float speed;

    EnumBookDesign(int id, int frames, float speed) {
        this.id = id;
        this.frames = frames;
        this.speed = speed;
    }

    public int getId() {
        return this.id;
    }

    public int getFrames() {
        return this.frames;
    }

    public float getSpeed() {
        return this.speed;
    }

}