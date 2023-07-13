# SectorProgressView
## A SectorProgressView like iOS download application

 ![Demo animation](https://github.com/luohaohaha/SectorProgressView/blob/main/images/image.gif)

## SectorProgressView attrs
```
<attr name="backgroundColor" format="color" />

<attr name="progressColor" format="color" />

<attr name="strokeWidth" format="dimension" />

<attr name="progress" format="float" />

<attr name="startAngle" format="float" />

<attr name="roundCorner" format="dimension" />

<attr name="sweepMode" format="enum">
 <enum name="obverse" value="0" />
 <enum name="reverse" value="1" />
</attr>
```

## Android xml
```
  <com.android.sectorprogressview.widget.SectorProgressView
                android:id="@+id/progress_view_reverse"
                android:layout_width="50dp"
                android:layout_height="50dp"
                android:layout_gravity="center"
                app:backgroundColor="#cc000000"
                app:progressColor="#cc000000"
                app:startAngle="0"
                app:strokeWidth="3dp"
                app:sweepMode="reverse" />
```
