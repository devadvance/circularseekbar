CircularSeekBar
===============

<h2>Android CircularSeekBar Custom View/Widget</h2>

This is a custom circular SeekBar. It can be used to create SeekBars that are:
-Full Circles
-Semi-Circles
-Ellipses
-Semi-Ellipses

In addition, it is fully customizable via XML attributes, far beyond the default values.

<h2>Documentation</h2>
In order to use the CircularSeekBar, you need to do two things.

<h3>Add Source Files to Project</h3>

There are two files you need to include:
-CircularSeekBar.java
-attrs.xml

Place the attrs.xml file inside of your res/values directory.
	
	../yourprojectdir/res/values/attrs.xml

Place CircularSeekBar.java inside of the src folder with the entire directory structure included:

	../yourprojectdir/src/com/devadvance/circularseekbar/CircularSeekBar.java

To use any methods inside if your classes, you must import the correct file. Add this to any classes you want to programmatically use the CircularSeekBar in:

	import com.devadvance.circularseekbar.CircularSeekBar;

<h3>Add CircularSeekBar to Your Layout</h3>

Start by adding this to the root of your layout xml file(s):

	xmlns:circle="http://schemas.android.com/apk/res-auto"

After you add it, the root of your layout xml file(s) should look SIMILAR to this:

	<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
		xmlns:tools="http://schemas.android.com/tools"
		xmlns:circle="http://schemas.android.com/apk/res-auto"
		android:layout_width="match_parent"
		android:layout_height="match_parent" >

To add the basic CircularSeekBar to your layout, add this to your layout xml file(s) where desired:

	<com.devadvance.circularseekbar.CircularSeekBar
	android:id="@+id/circularSeekBar1"
	android:layout_width="wrap_content"
	android:layout_height="wrap_content" />

For further configuration, such as color, sizes, and angle, you can specify many attributes associated with the CircularSeekBar, such as:
	
	circle:start_angle="270"
	circle:end_angle="270"
	circle:circle_x_radius="100"
	circle:circle_y_radius="100"
	circle:use_custom_radii="true"
	circle:progress="25"
	circle:max="100"
	circle:pointer_alpha_ontouch="100"
	circle:pointer_color="#0174DF"
	circle:pointer_halo_color="#880174DF"

<b>Note: all sizes are measured in DEGREES or DP. No pixels are used.</b>
        
You can also change the standard Android view attributes, such as:

	android:layout_width="300dp"
	android:layout_height="300dp"
	android:layout_marginTop="100dp"
	android:padding="0dp"

To use the CircularSeekBar programmatically, you can treat it like a normal SeekBar inside of your code:

	CircularSeekBar seekbar = (CircularSeekBar) findViewById(R.id.circularSeekBar1);
	seekbar.getProgress();
	seekbar.setProgress(50);

To use the listener to detect progress changes, first add the import for the class at the top of your file:
	
	import com.devadvance.circularseekbar.CircularSeekBar.OnCircularSeekBarChangeListener;

Next, create a listener that implements the OnCircularSeekBarChangeListener:

	public class CircleSeekBarListener implements OnCircularSeekBarChangeListener {
		@Override
		public void onProgressChanged(CircularSeekBar seekBar, int progress, boolean fromUser) {
			// TODO Insert your code here
			
		}
	}

Then create a new instance of it and set it for your seekbar:

	seekbar.setOnSeekBarChangeListener(new CircleSeekBarListener());

<h2>All Available Attributes</h2>

Available Attributes:

<b>progress</b> - Integer Value.

<b>max</b> - Integer Value.

<b>start_angle</b> - Decimal Value. Start angle of the circle. Angles are relative to 3 o'clock (positive X axis).

<b>end_angle</b> - Decimal Value. End angle of the circle. Angles are relative to 3 o'clock (positive X axis).

<b>maintain_equal_circle</b> - True/False. This controls whether or not an ellipse shape is available. Ellipses are not accurate in terms of representing progress, so be aware of that when you set this to false. Default value is true.

<b>use_custom_radii</b> - True/False. If true, then you can specify radii using attributes. No matter what is specified, they will not exceed the bounds of the View itself. If false, then the View size (android:layout_width and android:layout_height) is used.

<b>circle_x_radius</b> - Decimal Value. Custom X radius in DP.. Requires use_custom_radii to be true.

<b>circle_y_radius</b> - Decimal Value. Custom Y radius in DP. Requires use_custom_radii to be true.

<b>circle_stroke_width</b> - Decimal Value. Defines the width of the circle ring in DP.

<b>pointer_radius</b> - Decimal Value. Defines the radius of the pointer in DP.

<b>pointer_halo_width</b> - Decimal Value. Defines the width of the pointer halo in DP. Note: This is NOT a radius; it is in addition to the pointer radius.

<b>pointer_halo_border_width</b> - Decimal Value. Defines the width of the pointer halo border in DP. Note: This is NOT a radius; it is in addition to the pointer radius. The border shows up when the user is touching the CircularSeekBar.

<b>circle_color</b> - String value. Hex color value, can be #RRGGBB or #AARRGGBB (where AA is the alpha value).

<b>circle_progress_color</b> - String value. Hex color value, can be #RRGGBB or #AARRGGBB (where AA is the alpha value).

<b>pointer_color</b> - String value. Hex color value, can be #RRGGBB or #AARRGGBB (where AA is the alpha value).

<b>pointer_halo_color</b> - String value. Hex color value, can be #RRGGBB or #AARRGGBB (where AA is the alpha value). If no alpha is included, it defaults to 200 (out of 255).

<b>pointer_alpha_ontouch</b> - Integer value. When the user touches the CircularSeekBar, the opacity/alpha of the pointer halo changes to this value. Defaults to 100 (out of 255).
	
<h2>License</h2>
	
 	 Copyright 2013 Matt Joseph
 	
 	 Licensed under the Apache License, Version 2.0 (the "License");
 	 you may not use this file except in compliance with the License.
 	 You may obtain a copy of the License at
 	
 	     http://www.apache.org/licenses/LICENSE-2.0
 	
 	 Unless required by applicable law or agreed to in 	writing, software
	 distributed under the License is distributed on an "AS IS" BASIS,
 	 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, 	either express or implied.
 	 See the License for the specific language governing permissions and
 	 limitations under the License.
 	
 	
<h2>Inspired and Guided By</h2>
HoloCircleSeekBar - Copyright 2012 **Jesús Manzano**
HoloColorPicker - Copyright 2012 **Lars Werkman (Designed by Marie Schweiz)**

Although I did not used the code from either project directly, they were both used as reference material, and as a result, were extremely helpful.
