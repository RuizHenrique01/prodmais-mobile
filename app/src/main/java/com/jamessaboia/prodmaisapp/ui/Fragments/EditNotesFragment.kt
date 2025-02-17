package com.jamessaboia.prodmaisapp.ui.Fragments

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.NavigationUI
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import com.jamessaboia.prodmaisapp.Constants.Constants
import com.jamessaboia.prodmaisapp.Model.Login
import com.jamessaboia.prodmaisapp.Model.Notes
import com.jamessaboia.prodmaisapp.Model.TaskPost
import com.jamessaboia.prodmaisapp.R
import com.jamessaboia.prodmaisapp.ViewModel.NotesViewModel
import com.jamessaboia.prodmaisapp.ViewModel.TaskViewModel
import com.jamessaboia.prodmaisapp.databinding.FragmentEditNotesBinding
import java.util.*
import kotlin.random.Random

class EditNotesFragment : Fragment(), FragmentManager.OnBackStackChangedListener, AdapterView.OnItemSelectedListener {

    val oldNotes by navArgs<EditNotesFragmentArgs>()
    lateinit var binding: FragmentEditNotesBinding

    //  var priority: String = "1"
    val viewModel: TaskViewModel by viewModels()
    lateinit var navController: NavController

    private var status: Int = 0
    private var statusCurrent: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = FragmentEditNotesBinding.inflate(layoutInflater, container, false)
        setHasOptionsMenu(true)

        binding.edtTitle.setText(oldNotes.data.name)
        binding.edtNotes.setText(oldNotes.data.description)

        val adapter: ArrayAdapter<*> =
            ArrayAdapter.createFromResource(requireContext(), R.array.spinner_values, R.layout.spinner_items)
        adapter.setDropDownViewResource(R.layout.spinner_items);
        binding.edtStatus.setAdapter(adapter)

        binding.edtStatus.onItemSelectedListener = this

        if(oldNotes.data.status == "Do"){
            statusCurrent = 1
            binding.edtStatus.setSelection(0)
        } else if(oldNotes.data.status == "Doing"){
            statusCurrent = 2
            binding.edtStatus.setSelection(1)
        } else if(oldNotes.data.status == "Done"){
            statusCurrent = 3
            binding.edtStatus.setSelection(2)
        }

//        when (oldNotes.data.priority) {
//            "1" -> {
//                  priority = "1"
//                  binding.pGreen.setImageResource(R.drawable.ic_baseline_done_24)
//                  binding.pYellow.setImageResource(0)
//                  binding.pRed.setImageResource(0)
//            }
//            "2" -> {
//                  priority = "2"
//                  binding.pYellow.setImageResource(R.drawable.ic_baseline_done_24)
//                  binding.pGreen.setImageResource(0)
//                  binding.pRed.setImageResource(0
//            }
//            "3" -> {
//                  priority = "3"
//                  binding.pRed.setImageResource(R.drawable.ic_baseline_done_24)
//                  binding.pGreen.setImageResource(0)
//                  binding.pYellow.setImageResource(0)
//            }
//        }


//        binding.pGreen.setOnClickListener {
//            priority = "1"
//            binding.pGreen.setImageResource(R.drawable.ic_baseline_done_24)
//            binding.pYellow.setImageResource(0)
//            binding.pRed.setImageResource(0)
//        }
//        binding.pYellow.setOnClickListener {
//            priority = "2"
//            binding.pYellow.setImageResource(R.drawable.ic_baseline_done_24)
//            binding.pGreen.setImageResource(0)
//            binding.pRed.setImageResource(0)
//        }
//        binding.pRed.setOnClickListener {
//            priority = "3"
//            binding.pRed.setImageResource(R.drawable.ic_baseline_done_24)
//            binding.pGreen.setImageResource(0)
//            binding.pYellow.setImageResource(0)
//        }

        binding.btnEditSaveNotes.setOnClickListener {

            updateNotes(it)

        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(view)
    }

    private fun updateNotes(it: View?) {

        val title = binding.edtTitle.text.toString()
        val notes = binding.edtNotes.text.toString()


        if (title.isEmpty() || title.isBlank()){
            Toast.makeText(requireContext(), "Adicione um título a tarefa!", Toast.LENGTH_SHORT).show()
        } else {
            var data: TaskPost? = null

            if(notes.isEmpty() || notes.isBlank()){
                data = TaskPost(
                    title,
                    null,
                    status
                )
            } else {
                data = TaskPost(
                    title,
                    notes,
                    status
                )
            }

            Login.token?.let { it1 -> oldNotes?.data.id?.let { it2 ->
                Login.idBoard?.let { it3 ->
                    viewModel.putTask(it1, it3,
                        it2, data)
                }
            } }

            if(status == 3 && status != statusCurrent){

                val view = View.inflate(this.requireContext(), R.layout.dialog_quotes, null)


                val builder = AlertDialog.Builder(this.requireContext())
                builder.setView(view)

                val dialog = builder.create()
                dialog.show()
                dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

                val textMotivacional = view.findViewById<TextView>(R.id.tv_motivational)
                textMotivacional.setText(Constants.listMessage.get(Random.nextInt(0, Constants.listMessage.size - 1)))

                val buttonConfirm = view.findViewById<MaterialButton>(R.id.btn_confirm)
                buttonConfirm.setOnClickListener {
                    dialog.dismiss()
                }

//                val snackbar = Snackbar.make(it!!, Constants.listMessage.get(Random.nextInt(0, Constants.listMessage.size - 1)), Snackbar.LENGTH_INDEFINITE)
//
//                snackbar.setAction("OK", View.OnClickListener {
//                    snackbar.dismiss()
//                })
//
//                snackbar.show()

            } else{
                Toast.makeText(requireContext(), "Tarefa Editada com Sucesso!", Toast.LENGTH_SHORT).show()
            }

            Navigation.findNavController(it!!).navigate(R.id.action_editNotesFragment_to_homeFragment)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.delete_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_delete) {
            val bottonSheet: BottomSheetDialog =
                BottomSheetDialog(requireContext(), R.style.BottomSheetStyle)
            bottonSheet.setContentView(R.layout.dialog_delete)

            val textViewYes = bottonSheet.findViewById<TextView>(R.id.dialog_yes)
            val textViewNo = bottonSheet.findViewById<TextView>(R.id.dialog_no)

            bottonSheet.show()

            textViewYes?.setOnClickListener {
                Login.token?.let { it1 -> Login.idBoard?.let { it2 ->
                    viewModel.deleteTask(it1,
                        it2, oldNotes.data.id!!)
                } }

                Toast.makeText(requireContext(), "Tarefa Excluída com Sucesso!", Toast.LENGTH_SHORT).show()
                navController.navigate(R.id.action_editNotesFragment_to_homeFragment)
                bottonSheet.dismiss()
            }

            textViewNo?.setOnClickListener {
                bottonSheet.dismiss()
            }
        }
        return NavigationUI.onNavDestinationSelected(item!!,
            requireView().findNavController()) || super.onOptionsItemSelected(item)
    }

    override fun onBackStackChanged() {
        navController.navigate(R.id.action_editNotesFragment_to_homeFragment)
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        when(p0?.id){
            R.id.edtStatus ->{
                status = p2 + 1
            }
        }
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
    }

}