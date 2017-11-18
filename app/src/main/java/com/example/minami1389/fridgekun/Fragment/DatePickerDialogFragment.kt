package com.example.minami1389.fridgekun.Fragment

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.widget.DatePicker
import java.util.*


/**
 * Created by minami.baba on 2017/11/18.
 */

class DatePickerDialogFragment : DialogFragment(), DatePickerDialog.OnDateSetListener {

    var onDateSetCallback: ((year: Int, month: Int, day: Int) -> Unit)? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)

        return DatePickerDialog(getActivity(), this, year, month, dayOfMonth)

//        return super.onCreateDialog(savedInstanceState)
    }

    override fun onDateSet(view: DatePicker, year: Int, month: Int, day: Int) {
        onDateSetCallback?.invoke(year, month, day)
    }

//    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
//        val content = inflater?.inflate(R.layout.fragment_date_picker_dialog, null)
//        return content
//    }

}