package id.akhir.proyek.rukuntetangga.listener;

public abstract class MenuListener<T> {
    public abstract void onEdit(T data);
    public abstract void onDelete(T data);
}
