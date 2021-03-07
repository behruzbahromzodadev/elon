package tj.behruz.elon.ui

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import tj.behruz.elon.R
import tj.behruz.elon.databinding.ChooseFileBottomSheetBinding

class ChooseFileBottomSheet(val handler: (Int) -> Unit) : BottomSheetDialogFragment() {

    private lateinit var binding: ChooseFileBottomSheetBinding

    override fun getTheme(): Int = R.style.BottomSheetDialogTheme
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        BottomSheetDialog(requireContext(), theme)


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.choose_file_bottom_sheet,
            container,
            false
        )

        binding.lifecycleOwner = this

        return binding.root

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.takePictureFromCamera.setOnClickListener {
            handler.invoke(1)
            this.dismiss()
        }
        binding.takePictureFromGallery.setOnClickListener {
            handler.invoke(2)
            this.dismiss()

        }

    }


}