package BlocoDeNotas.trabalhofinaldesenvolvimentoandroidintermediario.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import BlocoDeNotas.trabalhofinaldesenvolvimentoandroidintermediario.data.TarefaRepository

class TarefaViewModelFactory(
    private val repository: TarefaRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {
        if (modelClass.isAssignableFrom(TarefaViewModel::class.java)) {
            return TarefaViewModel(repository) as T
        }

        throw IllegalArgumentException(
            "ViewModel desconhecido: ${modelClass.name}"
        )
    }
}