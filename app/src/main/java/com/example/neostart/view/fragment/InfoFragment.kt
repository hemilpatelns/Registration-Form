package com.example.neostart.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.neostart.R
import com.example.neostart.database.UserDatabase
import com.example.neostart.databinding.FragmentInfoBinding
import com.example.neostart.model.Info
import com.example.neostart.repository.UserRepository
import com.example.neostart.util.enums.Designation
import com.example.neostart.util.enums.Domain
import com.example.neostart.util.enums.Education
import com.example.neostart.util.enums.YearOfPassing
import com.example.neostart.util.setEnumAdapter
import com.example.neostart.viewmodel.UserViewModel
import com.example.neostart.viewmodel.UserViewModelFactory

class InfoFragment : Fragment() {

    private var _binding: FragmentInfoBinding? = null
    private val binding get() = _binding!!

    private lateinit var userViewModel: UserViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setToolbar()
        setEducationSpinner()
        setYearSpinner()
        setDesignationSpinner()
        setDomainSpinner()
        setViewModel()
        clickEvents()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setToolbar() {
        val toolbar: Toolbar = binding.tbInfo.tbApp
        (activity as AppCompatActivity).apply {
            setSupportActionBar(toolbar)
            supportActionBar?.apply {
                setDisplayHomeAsUpEnabled(true)
                setHomeAsUpIndicator(R.drawable.ic_back_arrow)
                setDisplayShowTitleEnabled(false)
                toolbar.setNavigationOnClickListener {
                    setDataToViewModel()
                    findNavController().navigateUp()
                }
                binding.tbInfo.tvToolbarTitle.text = getString(R.string.title_your_info)
            }
        }
    }

//    private fun yearOfPassing() {
//        val years = mutableListOf<String>()
//        val text = "Enter year of passing"
//        years.add("Enter year of passing")
//        years.addAll((1999..2024).map { it.toString() })
//
//        val adapter = ArrayAdapter(requireContext(), R.layout.item_spinner_option, years)
//        adapter.setDropDownViewResource(R.layout.layout_spinner_item)
//        binding.spnYearOfPassing.adapter = adapter
//    }

    private fun setEducationSpinner() {
        binding.spnEducation.setEnumAdapter(
            Education::class.java,
            { it.education }) { selectedEducation ->
//            Toast.makeText(
//                requireContext(),
//                "Selected Education: ${selectedEducation.education}",
//                Toast.LENGTH_SHORT
//            ).show()
        }
    }

    private fun setYearSpinner() {
        binding.spnYearOfPassing.setEnumAdapter(
            YearOfPassing::class.java,
            { it.year }) { selectedYear ->

        }
    }

    private fun setDesignationSpinner() {
        binding.spnDesignation.setEnumAdapter(
            Designation::class.java,
            { it.designation }) { selectedDesignation ->
//            Toast.makeText(
//                requireContext(),
//                "Selected Designation: ${selectedDesignation.designation}",
//                Toast.LENGTH_SHORT
//            ).show()
        }
    }

    private fun setDomainSpinner() {
        binding.spnDomain.setEnumAdapter(Domain::class.java, { it.domain }) { selectedDomain ->
//            Toast.makeText(
//                requireContext(),
//                "Selected Domain: ${selectedDomain.domain}",
//                Toast.LENGTH_SHORT
//            ).show()
        }
    }

    private fun setViewModel() {
        val database = UserDatabase.getDatabase(requireContext())
        val repository =
            UserRepository(database.registerDao(), database.infoDao(), database.addressDao())
        val factory = ViewModelProvider(requireActivity(), UserViewModelFactory(repository))
        userViewModel = factory[UserViewModel::class.java]

        userViewModel.infoData.observe(viewLifecycleOwner) { infoEntry ->
            if (infoEntry != null) {

//                var index = 0
//                Education.entries.forEachIndexed { i, education ->
//                    if (infoEntry.education==education.toString()){
//                        index = i
//                        return@forEachIndexed
//                    }
//                }
//                binding.spnEducation.setSelection(index)

                binding.spnEducation.setSelection(
                    Education.entries.indexOf(
                        Education.valueOf(
                            infoEntry.education
                        )
                    )
                )
                binding.spnYearOfPassing.setSelection(
                    YearOfPassing.entries.indexOf(
                        YearOfPassing.valueOf(
                            infoEntry.yearOfPassing
                        )
                    )
                )
                binding.edtGrade.setText(infoEntry.grade)
                binding.edtExperience.setText(infoEntry.experience)
                binding.spnDesignation.setSelection(
                    Designation.entries.indexOf(
                        Designation.valueOf(
                            infoEntry.designation
                        )
                    )
                )
                binding.spnDomain.setSelection(Domain.entries.indexOf(Domain.valueOf(infoEntry.domain)))
            }
        }
    }

    private fun clickEvents() {
        binding.btnPrevious.setOnClickListener {
            setDataToViewModel()
            findNavController().navigateUp()
        }
        binding.btnInfoNext.setOnClickListener {
            setDataToViewModel()
            findNavController().navigate(R.id.action_infoFragment_to_addressFragment)
        }
    }

    private fun setDataToViewModel() {
        val info = Info(
            education = Education.entries[binding.spnEducation.selectedItemPosition].toString(),
            yearOfPassing = YearOfPassing.entries[binding.spnYearOfPassing.selectedItemPosition].toString(),
            grade = binding.edtGrade.text.toString(),
            experience = binding.edtExperience.text.toString(),
            designation = Designation.entries[binding.spnDesignation.selectedItemPosition].toString(),
            domain = Domain.entries[binding.spnDomain.selectedItemPosition].toString()
        )
        userViewModel.setInfoData(info)
    }
}