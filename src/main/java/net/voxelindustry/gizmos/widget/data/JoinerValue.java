package net.voxelindustry.gizmos.widget.data;

public class JoinerValue extends MergeValue<String>
{
    public JoinerValue(String merged)
    {
        super(merged);
    }

    @Override
    public void merge(String toAdd)
    {
        setValue(getValue() + toAdd);
    }
}
