package com.example.neostart.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.fragment.findNavController
import com.example.neostart.R
import com.example.neostart.databinding.FragmentInfoBinding
import com.example.neostart.util.enums.Designation
import com.example.neostart.util.enums.Domain
import com.example.neostart.util.enums.Education
import com.example.neostart.util.enums.YearOfPassing
import com.example.neostart.util.setEnumAdapter

class InfoFragment : Fragment() {

    private var _binding: FragmentInfoBinding? = null
    private val binding get() = _binding!!

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
                    findNavController().navigateUp()
                }
                binding.tbInfo.tvToolbarTitle.text = getString(R.string.title_your_info)
            }
        }
    }

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
        binding.spnYearOfPassing.setEnumAdapter(YearOfPassing::class.java, {it.year}){ selectedYear ->

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

    private fun clickEvents() {
        binding.btnPrevious.setOnClickListener {
            findNavController().navigateUp()
        }
        binding.btnInfoNext.setOnClickListener {
            findNavController().navigate(R.id.action_infoFragment_to_addressFragment)
        }
    }
}