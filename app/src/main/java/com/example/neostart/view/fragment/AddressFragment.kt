package com.example.neostart.view.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.neostart.R
import com.example.neostart.database.UserDatabase
import com.example.neostart.databinding.FragmentAddressBinding
import com.example.neostart.model.Address
import com.example.neostart.repository.UserRepository
import com.example.neostart.util.enums.State
import com.example.neostart.util.setEnumAdapter
import com.example.neostart.viewmodel.UserViewModel
import com.example.neostart.viewmodel.UserViewModelFactory

class AddressFragment : Fragment() {

    private var _binding: FragmentAddressBinding? = null
    private val binding get() = _binding!!

    private lateinit var userViewModel: UserViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddressBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setToolbar()
        setStateSpinner()
        setViewModel()
        binding.btnSubmit.setOnClickListener {
            setDataToViewModel()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setToolbar() {
        val toolbar: Toolbar = binding.tbAddress.tbApp
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
                binding.tbAddress.tvToolbarTitle.text = getString(R.string.title_your_address)
            }
        }
    }

    private fun setStateSpinner(){
        binding.spnState.setEnumAdapter(State::class.java, {it.state}){ selectedState ->

        }
    }

    private fun setViewModel(){
        val database = UserDatabase.getDatabase(requireContext())
        val repository = UserRepository(database.registerDao(), database.infoDao(), database.addressDao())
        val factory = ViewModelProvider(requireActivity(), UserViewModelFactory(repository))
        userViewModel = factory[UserViewModel::class.java]

        userViewModel.addressData.observe(viewLifecycleOwner){addressEntity ->
            if(addressEntity != null){
                binding.edtAddress.setText(addressEntity.address)
                binding.edtLandmark.setText(addressEntity.landmark)
                binding.edtCity.setText(addressEntity.city)
                binding.spnState.setSelection(State.entries.indexOf(State.valueOf(addressEntity.state)))
                binding.edtPinCode.setText(addressEntity.pinCode)
            }
        }
    }

    private fun setDataToViewModel(){
        val address = Address(
            address = binding.edtAddress.text.toString(),
            landmark = binding.edtLandmark.text.toString(),
            city = binding.edtCity.text.toString(),
            state = State.entries[binding.spnState.selectedItemPosition].toString(),
            pinCode = binding.edtPinCode.text.toString()
        )
        userViewModel.setAddressData(address)
    }

}