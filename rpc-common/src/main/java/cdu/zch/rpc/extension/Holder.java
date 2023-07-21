package cdu.zch.rpc.extension;

/**
 * @author Zch
 * @date 2023/7/21
 **/
public class Holder<T> {

    private volatile T value;

    public T get() {
        return value;
    }

    public void set(T value) {
        this.value = value;
    }

}
