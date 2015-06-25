package de.htw.pim.sg.gui.util;

public class DataViewWrapper<T>
{
    private T data;
    
    private String view;

    public DataViewWrapper(T data, String view)
    {
        super();
        this.data = data;
        this.view = view;
    }

    public String getView()
    {
        return view;
    }

    public T getData()
    {
        return data;
    }

    @Override
    public String toString()
    {
        return view;
    }
}
