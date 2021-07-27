package id.akhir.proyek.rukuntetangga.listener;

public abstract class AdapterListener<t> {
    public abstract void onItemSelected(t data);
    public abstract void onItemLongSelected(t data);
}
