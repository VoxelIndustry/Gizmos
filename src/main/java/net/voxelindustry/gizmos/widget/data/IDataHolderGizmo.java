package net.voxelindustry.gizmos.widget.data;

import net.voxelindustry.gizmos.handle.GizmoHandle;

public interface IDataHolderGizmo
{
    void appendData(IGizmoDataPart dataPart, GizmoHandle handle);

    void removeData(IGizmoDataPart dataPart);
}
