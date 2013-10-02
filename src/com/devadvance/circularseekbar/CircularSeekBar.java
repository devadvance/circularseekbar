/*
 * 
 * Copyright 2013 Matt Joseph
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * 
 * 
 * This custom view/widget was inspired and guided by:
 * 
 * HoloCircleSeekBar - Copyright 2012 Jesús Manzano
 * HoloColorPicker - Copyright 2012 Lars Werkman (Designed by Marie Schweiz)
 * 
 * Although I did not used the code from either project directly, they were both used as 
 * reference material, and as a result, were extremely helpful.
 */

package com.devadvance.circularseekbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class CircularSeekBar extends View {

	/**
	 * Used to scale the dp units to pixels
	 */
	private final float DPTOPX_SCALE = getResources().getDisplayMetrics().density;

	/**
	 * Minimum touch target size in DP. 48dp is the Android design recommendation
	 */
	private final float MIN_TOUCH_TARGET_DP = 48;

	// Default values
	private static final float DEFAULT_CIRCLE_X_RADIUS = 30f;
	private static final float DEFAULT_CIRCLE_Y_RADIUS = 30f;
	private static final float DEFAULT_POINTER_RADIUS = 7f;
	private static final float DEFAULT_POINTER_HALO_WIDTH = 6f;
	private static final float DEFAULT_POINTER_HALO_BORDER_WIDTH = 2f;
	private static final float DEFAULT_CIRCLE_STROKE_WIDTH = 5f;
	private static final float DEFAULT_START_ANGLE = 270f; // Geometric (clockwise, relative to 3 o'clock)
	private static final float DEFAULT_END_ANGLE = 270f; // Geometric (clockwise, relative to 3 o'clock)
	private static final int DEFAULT_MAX = 100;
	private static final int DEFAULT_PROGRESS = 0;
	private static final int DEFAULT_CIRCLE_COLOR = Color.DKGRAY;
	private static final int DEFAULT_CIRCLE_PROGRESS_COLOR = Color.argb(235, 74, 138, 255);
	private static final int DEFAULT_POINTER_COLOR = Color.argb(235, 74, 138, 255);
	private static final int DEFAULT_POINTER_HALO_COLOR = Color.argb(135, 74, 138, 255);
	private static final int DEFAULT_POINTER_ALPHA = 135;
	private static final int DEFAULT_POINTER_ALPHA_ONTOUCH = 100;
	private static final boolean DEFAULT_USE_CUSTOM_RADII = false;
	private static final boolean DEFAULT_MAINTAIN_EQUAL_CIRCLE = true;
	private static final boolean DEFAULT_MOVE_OUTSIDE_CIRCLE = false;

	/**
	 * {@code Paint} instance used to draw the inactive circle.
	 */
	private Paint mCirclePaint;

	/**
	 * {@code Paint} instance used to draw the active circle (represents progress).
	 */
	private Paint mCircleProgressPaint;

	/**
	 * {@code Paint} instance used to draw the glow from the active circle.
	 */
	private Paint mCircleProgressGlowPaint;

	/**
	 * {@code Paint} instance used to draw the center of the pointer.
	 * Note: This is broken on 4.0+, as BlurMasks do not work with hardware acceleration.
	 */
	private Paint mPointerPaint;

	/**
	 * {@code Paint} instance used to draw the halo of the pointer.
	 * Note: The halo is the part that changes transparency.
	 */
	private Paint mPointerHaloPaint;

	/**
	 * {@code Paint} instance used to draw the border of the pointer, outside of the halo.
	 */
	private Paint mPointerHaloBorderPaint;

	/**
	 * The width of the circle (in pixels).
	 */
	private float mCircleStrokeWidth;

	/**
	 * The X radius of the circle (in pixels).
	 */
	private float mCircleXRadius;

	/**
	 * The Y radius of the circle (in pixels).
	 */
	private float mCircleYRadius;

	/**
	 * The radius of the pointer (in pixels).
	 */
	private float mPointerRadius;

	/**
	 * The width of the pointer halo (in pixels).
	 */
	private float mPointerHaloWidth;

	/**
	 * The width of the pointer halo border (in pixels).
	 */
	private float mPointerHaloBorderWidth;

	/**
	 * Start angle of the CircularSeekBar.
	 * Note: If mStartAngle and mEndAngle are set to the same angle, 0.1 is subtracted 
	 * from the mEndAngle to make the circle function properly.
	 */
	private float mStartAngle;

	/**
	 * End angle of the CircularSeekBar.
	 * Note: If mStartAngle and mEndAngle are set to the same angle, 0.1 is subtracted 
	 * from the mEndAngle to make the circle function properly.
	 */
	private float mEndAngle;

	/**
	 * {@code RectF} that represents the circle (or ellipse) of the seekbar.
	 */
	private RectF mCircleRectF = new RectF();

	/**
	 * Holds the color value for {@code mPointerPaint} before the {@code Paint} instance is created.
	 */
	private int mPointerColor = DEFAULT_POINTER_COLOR;

	/**
	 * Holds the color value for {@code mPointerHaloPaint} before the {@code Paint} instance is created.
	 */
	private int mPointerHaloColor = DEFAULT_POINTER_HALO_COLOR;

	/**
	 * Holds the color value for {@code mCirclePaint} before the {@code Paint} instance is created.
	 */
	private int mCircleColor = DEFAULT_CIRCLE_COLOR;

	/**
	 * Holds the color value for {@code mCircleProgressPaint} before the {@code Paint} instance is created.
	 */
	private int mCircleProgressColor = DEFAULT_CIRCLE_PROGRESS_COLOR;

	/**
	 * Holds the alpha value for {@code mPointerHaloPaint}.
	 */
	private int mPointerAlpha = DEFAULT_POINTER_ALPHA;

	/**
	 * Holds the OnTouch alpha value for {@code mPointerHaloPaint}.
	 */
	private int mPointerAlphaOnTouch = DEFAULT_POINTER_ALPHA_ONTOUCH;

	/**
	 * Distance (in degrees) that the the circle/semi-circle makes up.
	 * This amount represents the max of the circle in degrees.
	 */
	private float mTotalCircleDegrees;

	/**
	 * Distance (in degrees) that the current progress makes up in the circle.
	 */
	private float mProgressDegrees;

	/**
	 * {@code Path} used to draw the circle/semi-circle.
	 */
	private Path mCirclePath;

	/**
	 * {@code Path} used to draw the progress on the circle.
	 */
	private Path mCircleProgressPath;

	/**
	 * Max value that this CircularSeekBar is representing.
	 */
	private int mMax;

	/**
	 * Progress value that this CircularSeekBar is representing.
	 */
	private int mProgress;

	/**
	 * If true, then the user can specify the X and Y radii.
	 * If false, then the View itself determines the size of the CircularSeekBar.
	 */
	private boolean mCustomRadii;

	/**
	 * Maintain a perfect circle (equal x and y radius), regardless of view or custom attributes.
	 * The smaller of the two radii will always be used in this case.
	 * The default is to be a circle and not an ellipse, due to the behavior of the ellipse.
	 */
	private boolean mMaintainEqualCircle;

	/**
	 * Once a user has touched the circle, this determines if moving outside the circle is able 
	 * to change the position of the pointer (and in turn, the progress).
	 */
	private boolean mMoveOutsideCircle;

	/**
	 * Used for when the user moves beyond the start of the circle when moving counter clockwise.
	 * Makes it easier to hit the 0 progress mark.
	 */
	private boolean lockAtStart = true;

	/**
	 * Used for when the user moves beyond the end of the circle when moving clockwise.
	 * Makes it easier to hit the 100% (max) progress mark.
	 */
	private boolean lockAtEnd = false;

	/**
	 * When the user is touching the circle on ACTION_DOWN, this is set to true.
	 * Used when touching the CircularSeekBar.
	 */
	private boolean mUserIsMovingPointer = false;

	/**
	 * Represents the clockwise distance from {@code mStartAngle} to the touch angle.
	 * Used when touching the CircularSeekBar.
	 */
	private float cwDistanceFromStart;

	/**
	 * Represents the counter-clockwise distance from {@code mStartAngle} to the touch angle.
	 * Used when touching the CircularSeekBar.
	 */
	private float ccwDistanceFromStart;

	/**
	 * Represents the clockwise distance from {@code mEndAngle} to the touch angle.
	 * Used when touching the CircularSeekBar.
	 */
	private float cwDistanceFromEnd;

	/**
	 * Represents the counter-clockwise distance from {@code mEndAngle} to the touch angle.
	 * Used when touching the CircularSeekBar.
	 * Currently unused, but kept just in case.
	 */
	@SuppressWarnings("unused")
	private float ccwDistanceFromEnd;

	/**
	 * The previous touch action value for {@code cwDistanceFromStart}.
	 * Used when touching the CircularSeekBar.
	 */
	private float lastCWDistanceFromStart;

	/**
	 * True if the user is moving clockwise around the circle, false if moving counter-clockwise.
	 * Used when touching the CircularSeekBar.
	 */
	private boolean mIsMovingCW;

	/**
	 * The width of the circle used in the {@code RectF} that is used to draw it.
	 * Based on either the View width or the custom X radius.
	 */
	private float mCircleWidth;

	/**
	 * The height of the circle used in the {@code RectF} that is used to draw it.
	 * Based on either the View width or the custom Y radius.
	 */
	private float mCircleHeight;

	/**
	 * Represents the progress mark on the circle, in geometric degrees.
	 * This is not provided by the user; it is calculated;
	 */
	private float mPointerPosition;

	/**
	 * Pointer position in terms of X and Y coordinates.
	 */
	private float[] mPointerPositionXY = new float[2];

	/**
	 * Listener.
	 */
	private OnCircularSeekBarChangeListener mOnCircularSeekBarChangeListener;

	/**
	 * Initialize the CircularSeekBar with the attributes from the XML style.
	 * Uses the defaults defined at the top of this file when an attribute is not specified by the user.
	 * @param attrArray TypedArray containing the attributes.
	 */
	private void initAttributes(TypedArray attrArray) {
		mCircleXRadius = (float) (attrArray.getFloat(R.styleable.CircularSeekBar_circle_x_radius, DEFAULT_CIRCLE_X_RADIUS) * DPTOPX_SCALE);
		mCircleYRadius = (float) (attrArray.getFloat(R.styleable.CircularSeekBar_circle_y_radius, DEFAULT_CIRCLE_Y_RADIUS) * DPTOPX_SCALE);
		mPointerRadius = (float) (attrArray.getFloat(R.styleable.CircularSeekBar_pointer_radius, DEFAULT_POINTER_RADIUS) * DPTOPX_SCALE);
		mPointerHaloWidth = (float) (attrArray.getFloat(R.styleable.CircularSeekBar_pointer_halo_width, DEFAULT_POINTER_HALO_WIDTH) * DPTOPX_SCALE);
		mPointerHaloBorderWidth = (float) (attrArray.getFloat(R.styleable.CircularSeekBar_pointer_halo_border_width, DEFAULT_POINTER_HALO_BORDER_WIDTH) * DPTOPX_SCALE);
		mCircleStrokeWidth = (float) (attrArray.getFloat(R.styleable.CircularSeekBar_circle_stroke_width, DEFAULT_CIRCLE_STROKE_WIDTH) * DPTOPX_SCALE);

		String tempColor = attrArray.getString(R.styleable.CircularSeekBar_pointer_color);
		if (tempColor != null) {
			try {
				mPointerColor = Color.parseColor(tempColor);
			} catch (IllegalArgumentException e) {
				mPointerColor = DEFAULT_POINTER_COLOR;
			}
		}

		tempColor = attrArray.getString(R.styleable.CircularSeekBar_pointer_halo_color);
		if (tempColor != null) {
			try {
				mPointerHaloColor = Color.parseColor(tempColor);
			} catch (IllegalArgumentException e) {
				mPointerHaloColor = DEFAULT_POINTER_HALO_COLOR;
			}
		}

		tempColor = attrArray.getString(R.styleable.CircularSeekBar_circle_color);
		if (tempColor != null) {
			try {
				mCircleColor = Color.parseColor(tempColor);
			} catch (IllegalArgumentException e) {
				mCircleColor = DEFAULT_CIRCLE_COLOR;
			}
		}

		tempColor = attrArray.getString(R.styleable.CircularSeekBar_circle_progress_color);
		if (tempColor != null) {
			try {
				mCircleProgressColor = Color.parseColor(tempColor);
			} catch (IllegalArgumentException e) {
				mCircleProgressColor = DEFAULT_CIRCLE_PROGRESS_COLOR;
			}
		}

		mPointerAlpha = Color.alpha(mPointerHaloColor);

		mPointerAlphaOnTouch = attrArray.getInt(R.styleable.CircularSeekBar_pointer_alpha_ontouch, DEFAULT_POINTER_ALPHA_ONTOUCH);
		if (mPointerAlphaOnTouch > 255 || mPointerAlphaOnTouch < 0) {
			mPointerAlphaOnTouch = DEFAULT_POINTER_ALPHA_ONTOUCH;
		}

		mMax = attrArray.getInt(R.styleable.CircularSeekBar_max, DEFAULT_MAX);
		mProgress = attrArray.getInt(R.styleable.CircularSeekBar_progress, DEFAULT_PROGRESS);
		mCustomRadii = attrArray.getBoolean(R.styleable.CircularSeekBar_use_custom_radii, DEFAULT_USE_CUSTOM_RADII);
		mMaintainEqualCircle = attrArray.getBoolean(R.styleable.CircularSeekBar_maintain_equal_circle, DEFAULT_MAINTAIN_EQUAL_CIRCLE);
		mMoveOutsideCircle = attrArray.getBoolean(R.styleable.CircularSeekBar_move_outside_circle, DEFAULT_MOVE_OUTSIDE_CIRCLE);

		// Modulo 360 right now to avoid constant conversion
		mStartAngle = ((360f + (attrArray.getFloat((R.styleable.CircularSeekBar_start_angle), DEFAULT_START_ANGLE) % 360f)) % 360f);
		mEndAngle = ((360f + (attrArray.getFloat((R.styleable.CircularSeekBar_end_angle), DEFAULT_END_ANGLE) % 360f)) % 360f);

		if (mStartAngle == mEndAngle) {
			//mStartAngle = mStartAngle + 1f;
			mEndAngle = mEndAngle - .1f;
		}


	}

	/**
	 * Initializes the {@code Paint} objects with the appropriate styles.
	 */
	private void initPaints() {
		mCirclePaint = new Paint();
		mCirclePaint.setAntiAlias(true);
		mCirclePaint.setDither(true);
		mCirclePaint.setColor(mCircleColor);
		mCirclePaint.setStrokeWidth(mCircleStrokeWidth);
		mCirclePaint.setStyle(Paint.Style.STROKE);
		mCirclePaint.setStrokeJoin(Paint.Join.ROUND);
		mCirclePaint.setStrokeCap(Paint.Cap.ROUND);

		mCircleProgressPaint = new Paint();
		mCircleProgressPaint.setAntiAlias(true);
		mCircleProgressPaint.setDither(true);
		mCircleProgressPaint.setColor(mCircleProgressColor);
		mCircleProgressPaint.setStrokeWidth(mCircleStrokeWidth);
		mCircleProgressPaint.setStyle(Paint.Style.STROKE);
		mCircleProgressPaint.setStrokeJoin(Paint.Join.ROUND);
		mCircleProgressPaint.setStrokeCap(Paint.Cap.ROUND);

		mCircleProgressGlowPaint = new Paint();
		mCircleProgressGlowPaint.set(mCircleProgressPaint);
		mCircleProgressGlowPaint.setMaskFilter(new BlurMaskFilter((5f * DPTOPX_SCALE), BlurMaskFilter.Blur.NORMAL));

		mPointerPaint = new Paint();
		mPointerPaint.setAntiAlias(true);
		mPointerPaint.setDither(true);
		mPointerPaint.setStyle(Paint.Style.FILL);
		mPointerPaint.setColor(mPointerColor);
		mPointerPaint.setStrokeWidth(mPointerRadius);

		mPointerHaloPaint = new Paint();
		mPointerHaloPaint.set(mPointerPaint);
		mPointerHaloPaint.setColor(mPointerHaloColor);
		mPointerHaloPaint.setAlpha(mPointerAlpha);
		mPointerHaloPaint.setStrokeWidth(mPointerRadius + mPointerHaloWidth);

		mPointerHaloBorderPaint = new Paint();
		mPointerHaloBorderPaint.set(mPointerPaint);
		mPointerHaloBorderPaint.setStrokeWidth(mPointerHaloBorderWidth);
		mPointerHaloBorderPaint.setStyle(Paint.Style.STROKE);

	}

	/**
	 * Calculates the total degrees between mStartAngle and mEndAngle, and sets mTotalCircleDegrees 
	 * to this value.
	 */
	private void calculateTotalDegrees() {
		mTotalCircleDegrees = (360f - (mStartAngle - mEndAngle)) % 360f; // Length of the entire circle/arc
		if (mTotalCircleDegrees <= 0f) {
			mTotalCircleDegrees = 360f;
		}
	}

	/**
	 * Calculate the degrees that the progress represents. Also called the sweep angle.
	 * Sets mProgressDegrees to that value.
	 */
	private void calculateProgressDegrees() {
		mProgressDegrees = mPointerPosition - mStartAngle; // Verified
		mProgressDegrees = (mProgressDegrees < 0 ? 360f + mProgressDegrees : mProgressDegrees); // Verified
	}

	/**
	 * Calculate the pointer position (and the end of the progress arc) in degrees.
	 * Sets mPointerPosition to that value.
	 */
	private void calculatePointerAngle() {
		float progressPercent = ((float)mProgress / (float)mMax);
		mPointerPosition = (progressPercent * mTotalCircleDegrees) + mStartAngle;
		mPointerPosition = mPointerPosition % 360f;
	}

	private void calculatePointerXYPosition() {
		PathMeasure pm = new PathMeasure(mCircleProgressPath, false);
		boolean returnValue = pm.getPosTan(pm.getLength(), mPointerPositionXY, null);
		if (!returnValue) {
			pm = new PathMeasure(mCirclePath, false);
			returnValue = pm.getPosTan(0, mPointerPositionXY, null);
		}
	}

	/**
	 * Initialize the {@code Path} objects with the appropriate values. 
	 */
	private void initPaths() {
		mCirclePath = new Path();
		mCirclePath.addArc(mCircleRectF, mStartAngle, mTotalCircleDegrees);

		mCircleProgressPath = new Path();
		mCircleProgressPath.addArc(mCircleRectF, mStartAngle, mProgressDegrees);
	}

	/**
	 * Initialize the {@code RectF} objects with the appropriate values. 
	 */
	private void initRects() {
		mCircleRectF.set(-mCircleWidth, -mCircleHeight, mCircleWidth, mCircleHeight);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		canvas.translate(this.getWidth() / 2, this.getHeight() / 2);

		canvas.drawPath(mCirclePath, mCirclePaint);
		canvas.drawPath(mCircleProgressPath, mCircleProgressGlowPaint);
		canvas.drawPath(mCircleProgressPath, mCircleProgressPaint);

		canvas.drawCircle(mPointerPositionXY[0], mPointerPositionXY[1], mPointerRadius + mPointerHaloWidth, mPointerHaloPaint);
		canvas.drawCircle(mPointerPositionXY[0], mPointerPositionXY[1], mPointerRadius, mPointerPaint);
		if (mUserIsMovingPointer) {
			canvas.drawCircle(mPointerPositionXY[0], mPointerPositionXY[1], mPointerRadius + mPointerHaloWidth + (mPointerHaloBorderWidth / 2f), mPointerHaloBorderPaint);
		}
	}

	/**
	 * Get the progress of the CircularSeekBar.
	 * @return The progress of the CircularSeekBar.
	 */
	public int getProgress() {
		int progress = Math.round((float)mMax * mProgressDegrees / mTotalCircleDegrees);
		return progress;
	}

	/**
	 * Set the progress of the CircularSeekBar.
	 * If the progress is the same, then any listener will not receive a onProgressChanged event.
	 * @param progress The progress to set the CircularSeekBar to.
	 */
	public void setProgress(int progress) {
		if (mProgress != progress) {
			mProgress = progress;
			if (mOnCircularSeekBarChangeListener != null) {
				mOnCircularSeekBarChangeListener.onProgressChanged(this, progress, false);
			}

			recalculateAll();
			invalidate();
		}
	}

	private void setProgressBasedOnAngle(float angle) {
		mPointerPosition = angle;
		calculateProgressDegrees();
		mProgress = Math.round((float)mMax * mProgressDegrees / mTotalCircleDegrees);
	}

	private void recalculateAll() {
		calculateTotalDegrees();
		calculatePointerAngle();
		calculateProgressDegrees();

		initRects();

		initPaths();

		calculatePointerXYPosition();
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int height = getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec);
		int width = getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec);
		if (mMaintainEqualCircle) {
			int min = Math.min(width, height);
			setMeasuredDimension(min, min);
		} else {
			setMeasuredDimension(width, height);
		}

		// Set the circle width and height based on the view for the moment
		mCircleHeight = (float)height / 2f - mCircleStrokeWidth - mPointerRadius - (mPointerHaloBorderWidth * 1.5f);
		mCircleWidth = (float)width / 2f - mCircleStrokeWidth - mPointerRadius - (mPointerHaloBorderWidth * 1.5f);

		// If it is not set to use custom
		if (mCustomRadii) {
			// Check to make sure the custom radii are not out of the view. If they are, just use the view values
			if ((mCircleYRadius - mCircleStrokeWidth - mPointerRadius - mPointerHaloBorderWidth) < mCircleHeight) {
				mCircleHeight = mCircleYRadius - mCircleStrokeWidth - mPointerRadius - (mPointerHaloBorderWidth * 1.5f);
			}

			if ((mCircleXRadius - mCircleStrokeWidth - mPointerRadius - mPointerHaloBorderWidth) < mCircleWidth) {
				mCircleWidth = mCircleXRadius - mCircleStrokeWidth - mPointerRadius - (mPointerHaloBorderWidth * 1.5f);
			}
		}

		if (mMaintainEqualCircle) { // Applies regardless of how the values were determined
			float min = Math.min(mCircleHeight, mCircleWidth);
			mCircleHeight = min;
			mCircleWidth = min;
		}

		recalculateAll();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// Convert coordinates to our internal coordinate system
		float x = event.getX() - getWidth() / 2;
		float y = event.getY() - getHeight() / 2;

		// Get the distance from the center of the circle in terms of x and y
		float distanceX = mCircleRectF.centerX() - x;
		float distanceY = mCircleRectF.centerY() - y;

		// Get the distance from the center of the circle in terms of a radius
		float touchEventRadius = (float) Math.sqrt((Math.pow(distanceX, 2) + Math.pow(distanceY, 2)));

		float minimumTouchTarget = MIN_TOUCH_TARGET_DP * DPTOPX_SCALE; // Convert minimum touch target into px
		float additionalRadius; // Either uses the minimumTouchTarget size or larger if the ring/pointer is larger

		if (mCircleStrokeWidth < minimumTouchTarget) { // If the width is less than the minimumTouchTarget, use the minimumTouchTarget
			additionalRadius = minimumTouchTarget / 2;
		}
		else {
			additionalRadius = mCircleStrokeWidth / 2; // Otherwise use the width
		}
		float outerRadius = Math.max(mCircleHeight, mCircleWidth) + additionalRadius; // Max outer radius of the circle, including the minimumTouchTarget or wheel width
		float innerRadius = Math.min(mCircleHeight, mCircleWidth) - additionalRadius; // Min inner radius of the circle, including the minimumTouchTarget or wheel width

		if (mPointerRadius < (minimumTouchTarget / 2)) { // If the pointer radius is less than the minimumTouchTarget, use the minimumTouchTarget
			additionalRadius = minimumTouchTarget / 2;
		}
		else {
			additionalRadius = mPointerRadius; // Otherwise use the radius
		}

		float touchAngle;
		touchAngle = (float) ((java.lang.Math.atan2(y, x) / Math.PI * 180) % 360); // Verified
		touchAngle = (touchAngle < 0 ? 360 + touchAngle : touchAngle); // Verified

		cwDistanceFromStart = touchAngle - mStartAngle; // Verified
		cwDistanceFromStart = (cwDistanceFromStart < 0 ? 360f + cwDistanceFromStart : cwDistanceFromStart); // Verified
		ccwDistanceFromStart = 360f - cwDistanceFromStart; // Verified

		cwDistanceFromEnd = touchAngle - mEndAngle; // Verified
		cwDistanceFromEnd = (cwDistanceFromEnd < 0 ? 360f + cwDistanceFromEnd : cwDistanceFromEnd); // Verified
		ccwDistanceFromEnd = 360f - cwDistanceFromEnd; // Verified

		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			// If the user is touching outside of the start AND end
			if (cwDistanceFromStart > mTotalCircleDegrees) {
				mUserIsMovingPointer = false;
				return false;
			} else if ((touchEventRadius >= innerRadius) && (touchEventRadius <= outerRadius)) { // If the user is touching near the circle
				setProgressBasedOnAngle(touchAngle);
				lastCWDistanceFromStart = cwDistanceFromStart;
				mIsMovingCW = true;
				mPointerHaloPaint.setAlpha(mPointerAlphaOnTouch);
				recalculateAll();
				invalidate();
				if (mOnCircularSeekBarChangeListener != null) {
					mOnCircularSeekBarChangeListener.onProgressChanged(this, mProgress, true);
				}
				mUserIsMovingPointer = true;
				lockAtEnd = false;
				lockAtStart = false;
			} else { // If the user is not touching near the circle
				mUserIsMovingPointer = false;
				return false;
			}
			break;
		case MotionEvent.ACTION_MOVE:
			if (mUserIsMovingPointer) {
				if (lastCWDistanceFromStart < cwDistanceFromStart) {
					if ((cwDistanceFromStart - lastCWDistanceFromStart) > 180f && !mIsMovingCW) {
						lockAtStart = true;
						lockAtEnd = false;
					} else {
						mIsMovingCW = true;
					}
				} else {
					if ((lastCWDistanceFromStart - cwDistanceFromStart) > 180f && mIsMovingCW) {
						lockAtEnd = true;
						lockAtStart = false;
					} else {
						mIsMovingCW = false;
					}
				}

				if (lockAtStart && mIsMovingCW) {
					lockAtStart = false;
				}
				if (lockAtEnd && !mIsMovingCW) {
					lockAtEnd = false;
				}
				if (lockAtStart && !mIsMovingCW && (ccwDistanceFromStart > 90)) {
					lockAtStart = false;
				}
				if (lockAtEnd && mIsMovingCW && (cwDistanceFromEnd > 90)) {
					lockAtEnd = false;
				}
				// Fix for passing the end of a semi-circle quickly
				if (!lockAtEnd && cwDistanceFromStart > mTotalCircleDegrees && mIsMovingCW && lastCWDistanceFromStart < mTotalCircleDegrees) {
					lockAtEnd = true;
				}

				if (lockAtStart) {
					// TODO: Add a check if mProgress is already 0, in which case don't call the listener
					mProgress = 0;
					recalculateAll();
					invalidate();
					if (mOnCircularSeekBarChangeListener != null) {
						mOnCircularSeekBarChangeListener.onProgressChanged(this, mProgress, true);
					}
				} else if (lockAtEnd) {
					mProgress = mMax;
					recalculateAll();
					invalidate();
					if (mOnCircularSeekBarChangeListener != null) {
						mOnCircularSeekBarChangeListener.onProgressChanged(this, mProgress, true);
					}
				} else if ((mMoveOutsideCircle) || (touchEventRadius <= outerRadius)) {
					if (!(cwDistanceFromStart > mTotalCircleDegrees)) {
						setProgressBasedOnAngle(touchAngle);
					}
					recalculateAll();
					invalidate();
					if (mOnCircularSeekBarChangeListener != null) {
						mOnCircularSeekBarChangeListener.onProgressChanged(this, mProgress, true);
					}
				} else {
					break;
				}

				lastCWDistanceFromStart = cwDistanceFromStart;
			} else {
				return false;
			}
			break;
		case MotionEvent.ACTION_UP:
			mPointerHaloPaint.setAlpha(mPointerAlpha);
			if (mUserIsMovingPointer) {
				mUserIsMovingPointer = false;
				invalidate();
				if (mOnCircularSeekBarChangeListener != null) {
					mOnCircularSeekBarChangeListener.onProgressChanged(this, mProgress, true);
				}
			} else {
				return false;
			}
			break;
		}

		if (event.getAction() == MotionEvent.ACTION_MOVE && getParent() != null) {
			getParent().requestDisallowInterceptTouchEvent(true);
		}

		return true;
	}

	private void init(AttributeSet attrs, int defStyle) {
		final TypedArray attrArray = getContext().obtainStyledAttributes(attrs, R.styleable.CircularSeekBar, defStyle, 0);

		initAttributes(attrArray);

		attrArray.recycle();

		initPaints();
	}

	public CircularSeekBar(Context context) {
		super(context);
		init(null, 0);
	}

	public CircularSeekBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(attrs, 0);
	}

	public CircularSeekBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(attrs, defStyle);
	}

	@Override
	protected Parcelable onSaveInstanceState() {
		Parcelable superState = super.onSaveInstanceState();

		Bundle state = new Bundle();
		state.putParcelable("PARENT", superState);
		state.putInt("MAX", mMax);
		state.putInt("PROGRESS", mProgress);

		return state;
	}

	@Override
	protected void onRestoreInstanceState(Parcelable state) {
		Bundle savedState = (Bundle) state;

		Parcelable superState = savedState.getParcelable("PARENT");
		super.onRestoreInstanceState(superState);

		mMax = savedState.getInt("MAX");
		mProgress = savedState.getInt("PROGRESS");

		recalculateAll();
	}


	public void setOnSeekBarChangeListener(OnCircularSeekBarChangeListener l) {
		mOnCircularSeekBarChangeListener = l;
	}

	public interface OnCircularSeekBarChangeListener {

		public abstract void onProgressChanged(CircularSeekBar seekBar, int progress, boolean fromUser);

	}
}
