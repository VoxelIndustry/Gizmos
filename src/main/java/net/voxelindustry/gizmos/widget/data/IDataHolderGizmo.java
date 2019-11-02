package net.voxelindustry.gizmos.widget.data;

import net.voxelindustry.gizmos.handle.GizmoHandle;

public interface IDataHolderGizmo
{
    void appendData(String text, GizmoHandle handle, Object... values);

    void removeData(String text);

    <T> void updateValue(String key, T value);
}
