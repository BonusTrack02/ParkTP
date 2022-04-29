package com.bonustrack02.parktp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bonustrack02.parktp.databinding.FragmentGraphBinding
import com.kizitonwose.calendarview.model.CalendarDay
import com.kizitonwose.calendarview.ui.DayBinder
import java.time.YearMonth
import java.time.temporal.WeekFields
import java.util.*

class GraphFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding.root
    }

    val binding : FragmentGraphBinding by lazy { FragmentGraphBinding.inflate(layoutInflater) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.calendarView.dayBinder = object : DayBinder<DayViewContainer> {
            override fun bind(container: DayViewContainer, day: CalendarDay) {
                container.textView.text = day.date.dayOfMonth.toString()
            }

            override fun create(view: View) = DayViewContainer(view)
        }

        val curMonth = YearMonth.now()
        val firstMonth = curMonth.minusMonths(10)
        val lastMonth = curMonth.plusMonths(10)
        val firstDayOfWeek = WeekFields.of(Locale.getDefault()).firstDayOfWeek
        binding.calendarView.setup(firstMonth, lastMonth, firstDayOfWeek)
        binding.calendarView.scrollToMonth(curMonth)
    }
}