package net.voxelindustry.gizmos.widget.data;

public class CounterValue extends MergeValue<Integer>
{
    public CounterValue(Integer merged)
    {
        super(merged);
    }

    @Override
    public void merge(Integer toAdd)
    {
        setValue(getValue() + toAdd);
    }
}
