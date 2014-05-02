CircularSeekBar
===============

<h2>Android CircularSeekBar Custom View/Widget</h2>

This is a custom circular SeekBar. It can be used to create SeekBars that are:

-Full Circles

-Semi-Circles

-Ellipses

-Semi-Ellipses


In addition, it is fully customizable via XML attributes, far beyond the default values.

<h2>Support and Testing</h2>

Tested and working on Android 2.3+.

Tested on the following devices:

-HTC One Google Edition (Android 4.3)

-HTC One X (AT&T, Android 4.1)

-Galaxy Nexus (Android 4.3)

-Emulator with Android versions 2.3-4.2.

CircularSeekBar has also been tested with normal scrolling. It was also been tested successfully with Fragments, as well as scrolling tabs + Fragments.


Known Issues:
-The glow effect around the progress part of the circle will not show up on Android 4.0+. This is due to the BlurMaskFilter not working when hardware acceleration is on. Android OS issue.

![Screenshot 1](https://lh3.googleusercontent.com/-1JEeXRNfhYc/Ukiy_myxcoI/AAAAAAAAJD0/k8nGDGxfg7k/w281-h500-no/1.png)

<h2>Documentation</h2>
In order to use the CircularSeekBar, you need to do three things.

<h3>1) Add Source Files to Project</h3>

There are two files you need to include:
-CircularSeekBar.java
-attrs.xml

Place the attrs.xml file inside of your res/values directory.
	
	../yourprojectdir/res/values/attrs.xml

Place CircularSeekBar.java inside of the src folder with the entire directory structure included:

	../yourprojectdir/src/com/devadvance/circularseekbar/CircularSeekBar.java

<h3>2) Customize Source Files</h3>

To use any methods inside if your classes, you must import the correct file. Add this to any classes you want to programmatically use the CircularSeekBar in:

	import com.devadvance.circularseekbar.CircularSeekBar;

You also need to add the import for the generated R class to the CircularSeekBar class. You need to change the top of the CircularSeekBar.java file to look like this:

	package com.devadvance.circularseekbar;

	import com.example.yourappname.R;

where <b>"com.example.yourappname"</b> depends on your project/app. Eclipse *may* offer to add this import for you.


<h3>3) Add CircularSeekBar to Your Layout</h3>

Start by adding this to the root of your layout xml file(s):

	xmlns:app="http://schemas.android.com/apk/res/com.devadvance.circulartest"

After you add it, the root of your layout xml file(s) should look SIMILAR to this:

	<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
		xmlns:tools="http://schemas.android.com/tools"
		xmlns:app="http://schemas.android.com/apk/res/com.devadvance.circulartest"
		android:layout_width="match_parent"
		android:layout_height="match_parent" >

To add the basic CircularSeekBar to your layout, add this to your layout xml file(s) where desired:

	<com.devadvance.circularseekbar.CircularSeekBar
	android:id="@+id/circularSeekBar1"
	android:layout_width="wrap_content"
	android:layout_height="wrap_content" />

For further configuration, such as color, sizes, and angle, you can specify many attributes associated with the CircularSeekBar, such as:
	
	app:start_angle="270"
	app:end_angle="270"
	app:circle_x_radius="100"
	app:circle_y_radius="100"
	app:use_custom_radii="true"
	app:progress="25"
	app:max="100"
	app:pointer_alpha_ontouch="100"
	app:pointer_color="#0174DF"
	app:pointer_halo_color="#880174DF"

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
		public void onProgressChanged(CircularSeekBar circularSeekBar, int progress, boolean fromUser) {
			// TODO Insert your code here
			
		}
	}

Then create a new instance of it and set it for your seekbar:

	seekbar.setOnSeekBarChangeListener(new CircleSeekBarListener());

<h2>All Available Attributes</h2>

Available Attributes:

<b>progress</b> - Integer Value.

<b>max</b> - Integer Value.

<b>move_outside_circle</b> - True/False. Default is false. In the case that the user has already touched down on the CircularSeekBar and is adjusting the progress, this determines whether or not movement outside the circle is accepted and adjusted the progress.

<b>start_angle</b> - Decimal Value. Start angle of the circle. Angles are relative to 3 o'clock (positive X axis).

<b>end_angle</b> - Decimal Value. End angle of the circle. Angles are relative to 3 o'clock (positive X axis).

<b>maintain_equal_circle</b> - True/False. Default is true. This controls whether or not an ellipse shape is available. Ellipses are not accurate in terms of representing progress, so be aware of that when you set this to false. Default value is true.

<b>use_custom_radii</b> - True/False. Default is false. If true, then you can specify radii using attributes. No matter what is specified, they will not exceed the bounds of the View itself. If false, then the View size (android:layout_width and android:layout_height) is used.

<b>lock_enabled</b> - True/False. Default is true. If true, then the progress will "stick" to the start/end point. If false, it'll just pass through.

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


<h2>All Available Methods</h2>

The methods available to View are all present, as well as some custom methods that allow customization programmatically:

<b>setProgress</b> and <b>getProgress</b>

<b>setMax</b> and <b>getMax</b>

<b>setCircleColor</b> and <b>getCircleColor</b>

<b>setCircleProgressColor</b> and <b>getCircleProgressColor</b>

<b>setPointerColor</b> and <b>getPointerColor</b>

<b>setPointerHaloColor</b> and <b>getPointerHaloColor</b>

<b>setPointerAlpha</b> and <b>getPointerAlpha</b>

<b>setPointerAlphaOnTouch</b> and <b>getPointerAlphaOnTouch</b>

<b>setLockEnabled</b> and <b>isLockEnabled</b>

Note: Changes made with these methods are persisted by saving state.


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

<h2>Other Screenshots<h2>
![Screenshot 2](https://lh6.googleusercontent.com/-mq64-hEt_KM/Ukiy_swcllI/AAAAAAAAJD8/jy7pyKaFAaQ/w281-h500-no/2.png)

![Screenshot 3](https://lh6.googleusercontent.com/-amwGemq-PDQ/Ukiy_rP832I/AAAAAAAAJEE/BjKuqqQVzeQ/w281-h500-no/3.png)

![Screenshot 4](https://lh5.googleusercontent.com/-Mh-_AoEunxw/UkizAF-T5HI/AAAAAAAAJEI/tR0Sl5vCLVo/w281-h500-no/4.png)

Content originally by: devadvance (<a href="https://plus.google.com/115147267178456662187?rel=author">+Matt Joseph</a>)