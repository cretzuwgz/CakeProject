/*
* Copyright 2014 Google Inc. All rights reserved.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
package com.teentitans.cakeproject.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;

class SlidingTabStrip extends LinearLayout {
    private static final int DEFAULT_BOTTOM_BORDER_THICKNESS_DIPS = 0;
    private static final byte DEFAULT_BOTTOM_BORDER_COLOR_ALPHA = 0x26;
    private static final int SELECTED_INDICATOR_THICKNESS_DIPS = 3;
    private static final int DEFAULT_SELECTED_INDICATOR_COLOR = 0xFF33B5E5;
    private final int _bottomBorderThickness;
    private final Paint _bottomBorderPaint;
    private final int _selectedIndicatorThickness;
    private final Paint _selectedIndicatorPaint;
    private final SimpleTabColorizer _defaultTabColorizer;
    private int _selectedPosition;
    private float _selectionOffset;
    private SlidingTabLayout.TabColorizer _customTabColorizer;

    SlidingTabStrip(Context context) {

        this(context, null);
    }

    SlidingTabStrip(Context context, AttributeSet attrs) {

        super(context, attrs);
        setWillNotDraw(false);
        final float density = getResources().getDisplayMetrics().density;
        TypedValue outValue = new TypedValue();
        context.getTheme().resolveAttribute(android.R.attr.colorForeground, outValue, true);
        final int themeForegroundColor = outValue.data;
        int defaultBottomBorderColor = setColorAlpha(themeForegroundColor,
                DEFAULT_BOTTOM_BORDER_COLOR_ALPHA);
        _defaultTabColorizer = new SimpleTabColorizer();
        _defaultTabColorizer.setIndicatorColors(DEFAULT_SELECTED_INDICATOR_COLOR);
        _bottomBorderThickness = (int) (DEFAULT_BOTTOM_BORDER_THICKNESS_DIPS * density);
        _bottomBorderPaint = new Paint();
        _bottomBorderPaint.setColor(defaultBottomBorderColor);
        _selectedIndicatorThickness = (int) (SELECTED_INDICATOR_THICKNESS_DIPS * density);
        _selectedIndicatorPaint = new Paint();
    }

    /**
     * Set the alpha value of the {@code color} to be the given {@code alpha} value.
     */
    private static int setColorAlpha(int color, byte alpha) {

        return Color.argb(alpha, Color.red(color), Color.green(color), Color.blue(color));
    }

    /**
     * Blend {@code color1} and {@code color2} using the given ratio.
     *
     * @param ratio of which to blend. 1.0 will return {@code color1}, 0.5 will give an even blend,
     *              0.0 will return {@code color2}.
     */
    private static int blendColors(int color1, int color2, float ratio) {

        final float inverseRation = 1f - ratio;
        float r = (Color.red(color1) * ratio) + (Color.red(color2) * inverseRation);
        float g = (Color.green(color1) * ratio) + (Color.green(color2) * inverseRation);
        float b = (Color.blue(color1) * ratio) + (Color.blue(color2) * inverseRation);
        return Color.rgb((int) r, (int) g, (int) b);
    }

    void setCustomTabColorizer(SlidingTabLayout.TabColorizer customTabColorizer) {

        _customTabColorizer = customTabColorizer;
        invalidate();
    }

    void setSelectedIndicatorColors(int... colors) {

        // Make sure that the custom colorizer is removed
        _customTabColorizer = null;
        _defaultTabColorizer.setIndicatorColors(colors);
        invalidate();
    }

    void onViewPagerPageChanged(int position, float positionOffset) {

        _selectedPosition = position;
        _selectionOffset = positionOffset;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {

        final int height = getHeight();
        final int childCount = getChildCount();
        final SlidingTabLayout.TabColorizer tabColorizer = _customTabColorizer != null
                ? _customTabColorizer
                : _defaultTabColorizer;
        // Thick colored underline below the current selection
        if (childCount > 0) {
            View selectedTitle = getChildAt(_selectedPosition);
            int left = selectedTitle.getLeft();
            int right = selectedTitle.getRight();
            int color = tabColorizer.getIndicatorColor(_selectedPosition);
            if (_selectionOffset > 0f && _selectedPosition < (getChildCount() - 1)) {
                int nextColor = tabColorizer.getIndicatorColor(_selectedPosition + 1);
                if (color != nextColor) {
                    color = blendColors(nextColor, color, _selectionOffset);
                }
                // Draw the selection partway between the tabs
                View nextTitle = getChildAt(_selectedPosition + 1);
                left = (int) (_selectionOffset * nextTitle.getLeft() +
                        (1.0f - _selectionOffset) * left);
                right = (int) (_selectionOffset * nextTitle.getRight() +
                        (1.0f - _selectionOffset) * right);
            }
            _selectedIndicatorPaint.setColor(color);
            canvas.drawRect(left, height - _selectedIndicatorThickness, right,
                    height, _selectedIndicatorPaint);
        }
        // Thin underline along the entire bottom edge
        canvas.drawRect(0, height - _bottomBorderThickness, getWidth(), height, _bottomBorderPaint);
    }

    private static class SimpleTabColorizer implements SlidingTabLayout.TabColorizer {

        private int[] _indicatorColors;

        @Override
        public final int getIndicatorColor(int position) {

            return _indicatorColors[position % _indicatorColors.length];
        }

        void setIndicatorColors(int... colors) {

            _indicatorColors = colors;
        }
    }
}