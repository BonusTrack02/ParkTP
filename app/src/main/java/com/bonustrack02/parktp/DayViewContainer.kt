package com.bonustrack02.parktp

import android.view.View
import com.bonustrack02.parktp.databinding.CalendarDayLayoutBinding
import com.google.android.material.textview.MaterialTextView
import com.kizitonwose.calendarview.ui.ViewContainer

class DayViewContainer(view : View) : ViewContainer(view) {
    val textView = CalendarDayLayoutBinding.bind(view).calendarDayText


}