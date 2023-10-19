
package com.example.android.unscramble.ui.game
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.android.unscramble.R
import com.example.android.unscramble.R.layout.game_fragment
import com.example.android.unscramble.databinding.GameFragmentBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

/**
 * Fragmento que muestra el juego y permite a los jugadores interactuar con él.
 */

class GameFragment : Fragment() {
    // ViewModel para gestionar la lógica del juego
    private val viewModel: GameViewModel by viewModels()

    // Variable para acceder a las vistas en el diseño
    private lateinit var binding: GameFragmentBinding

    /**
     * Se llama al crear la vista del fragmento.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Infla la vista y enlaza con las vistas en el diseño
        binding = DataBindingUtil.inflate(inflater,R.layout.game_fragment, container, false)
        Log.d("GameFragment", "GameFragment created/re-created!")
        Log.d("GameFragment", "Word: ${viewModel.currentScrambledWord} " +
                "Score: ${viewModel.score} WordCount: ${viewModel.currentWordCount}")
        return binding.root
    }

    /**
     * Se llama cuando la vista se ha creado completamente.
     *
     * viewModel.currentScrambledWord.observe(viewLifecycleOwner) { newWord ->
     *             binding.textViewUnscrambledWord.text = newWord
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.gameViewModel = viewModel
        binding.maxNoOfWords = MAX_NO_OF_WORDS

        // Configura los clics de los botones
        binding.submit.setOnClickListener { onSubmitWord() }
        binding.skip.setOnClickListener { onSkipWord() }

        binding.lifecycleOwner = viewLifecycleOwner
    }

        private fun onSubmitWord() {
        val playerWord = binding.textInputEditText.text.toString()

        if (viewModel.isUserWordCorrect(playerWord)) {
            setErrorTextField(false)
            if (viewModel.nextWord())
        showFinalScoreDialog()
        } else {
            setErrorTextField(true)
        }
    }


    private fun onSkipWord() {
        if (viewModel.nextWord()) {
            setErrorTextField(false)
        } else {
            showFinalScoreDialog()
        }
    }

    /**
     * Muestra un cuadro de diálogo con la puntuación final del jugador.
     */
    private fun showFinalScoreDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.congratulations))
            .setMessage(getString(R.string.you_scored, viewModel.score.value))
            .setCancelable(false)
            .setNegativeButton(getString(R.string.exit)) { _, _ ->
                exitGame()
            }
            .setPositiveButton(getString(R.string.play_again)) { _, _ ->
                restartGame()
            }
            .show()
    }


    private fun restartGame() {
        viewModel.reinitializeData()
        setErrorTextField(false)

    }


    private fun exitGame() {
        activity?.finish()
    }


    private fun setErrorTextField(error: Boolean) {
        if (error) {
            binding.textField.isErrorEnabled = true
            binding.textField.error = getString(R.string.try_again)
        } else {
            binding.textField.isErrorEnabled = false
            binding.textInputEditText.text = null
        }
    }

}