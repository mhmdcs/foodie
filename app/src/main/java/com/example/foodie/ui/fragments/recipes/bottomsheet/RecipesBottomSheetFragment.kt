package com.example.foodie.ui.fragments.recipes.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.get
import androidx.lifecycle.lifecycleScope
import com.example.foodie.DEFAULT_DIET_TYPE
import com.example.foodie.DEFAULT_MEAL_TYPE
import com.example.foodie.R
import com.example.foodie.databinding.FragmentRecipesBottomSheetBinding
import com.example.foodie.viewmodels.RecipesViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip
import kotlinx.coroutines.launch
import java.util.*


class RecipesBottomSheetFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentRecipesBottomSheetBinding? = null
    private val binding get() = _binding!!

    private val viewModel by lazy { ViewModelProvider(requireActivity())[RecipesViewModel::class.java] } // alternatively, `by viewModels<>() does exactly the same thing, it lazily initializes viewModels via ViewModelProvider, with shorter syntax.

    private var mealTypeChip = DEFAULT_MEAL_TYPE
    private var mealTypeChipId = 0
    private var dietTypeChip = DEFAULT_DIET_TYPE
    private var dietTypeChipId = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentRecipesBottomSheetBinding.inflate(inflater, container, false)

        // capture the selected Meal Chip's text and id and store them in the global member variables
        binding.mealTypeChipGroup.setOnCheckedStateChangeListener { group, _ ->
            val chip =
                group.findViewById<Chip>(group.checkedChipId) // alternatively, pass in selectedChipIds.first()
            val selectedMealType =
                chip.text.toString().lowercase(Locale.ROOT) // or Locale.getDefault() ?
            mealTypeChip = selectedMealType
            mealTypeChipId = group.checkedChipId
        }

        // capture the selected Diet Chip's text and id and store them in the global member variables
        binding.dietTypeChipGroup.setOnCheckedStateChangeListener { group, _ ->
            val chip = group.findViewById<Chip>(group.checkedChipId)
            val selectedDietType =
                chip.text.toString().lowercase(Locale.ROOT) // or Locale.getDefault() ?
            dietTypeChip = selectedDietType
            dietTypeChipId = group.checkedChipId
        }

        // save the selected Meal and Diet Chips to the Preferences DataStore
        binding.applyBtn.setOnClickListener {
            viewModel.saveMealAndDietType(
                mealTypeChip,
                mealTypeChipId,
                dietTypeChip,
                dietTypeChipId
            )
        }

        // read the selected Meal and Diet Chips from the Preferences DataStore and then set them as checked Chips
        viewModel.readMealAndDietType.asLiveData().observe(viewLifecycleOwner) { value ->
            mealTypeChip = value.selectedMealType
            mealTypeChipId = value.selectedMealTypeId
            dietTypeChip = value.selectedDietType
            dietTypeChipId = value.selectedDietTypeId

            if(mealTypeChipId != 0 ) {
                binding.mealTypeChipGroup.findViewById<Chip>(mealTypeChipId).isChecked = true
            }
            if(dietTypeChipId != 0 ) {
                binding.dietTypeChipGroup.findViewById<Chip>(dietTypeChipId).isChecked = true
            }
        }
        // or just collect the flows normally, both work
//        lifecycleScope.launch {
//            viewModel.readMealAndDietType.collect {
//                mealTypeChip = it.selectedMealType
//                mealTypeChipId = it.selectedMealTypeId
//                dietTypeChip = it.selectedDietType
//                dietTypeChipId = it.selectedDietTypeId
//
//                if(mealTypeChipId != 0 ) {
//                    binding.mealTypeChipGroup.findViewById<Chip>(mealTypeChipId).isChecked = true
//                }
//                if(dietTypeChipId != 0 ) {
//                    binding.dietTypeChipGroup.findViewById<Chip>(dietTypeChipId).isChecked = true
//                }
//            }
//        }

        return binding.root
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}