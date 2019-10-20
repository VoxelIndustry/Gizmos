package net.voxelindustry.gizmos.widget.data;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public abstract class MergeValue<T>
{
    private T merged;

    public abstract void merge(T toAdd);

    public T getValue()
    {
        return merged;
    }

    protected void setValue(T value)
    {
        merged = value;
    }
}
