package BlocoDeNotas.trabalhofinaldesenvolvimentoandroidintermediario.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import BlocoDeNotas.trabalhofinaldesenvolvimentoandroidintermediario.data.Tarefa
import BlocoDeNotas.trabalhofinaldesenvolvimentoandroidintermediario.data.TarefaRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class TarefaViewModel(
    private val repository: TarefaRepository
) : ViewModel() {

    val tarefas: StateFlow<List<Tarefa>> =
        repository.tarefas.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )


    fun adicionarTarefa(
        titulo: String,
        detalhes: String,
        cor: String
    ) {
        viewModelScope.launch {
            repository.inserir(
                Tarefa(
                    titulo = titulo,
                    detalhes = detalhes,
                    cor = cor
                )
            )
        }
    }


    fun excluirTarefa(tarefa: Tarefa) {
        viewModelScope.launch {
            repository.excluir(tarefa)
        }
    }

    fun excluirTodas() {
        viewModelScope.launch {
            repository.excluirTodas()
        }
    }

    fun concluirTarefa(tarefa: Tarefa) {
        viewModelScope.launch {
            repository.atualizar(
                tarefa.copy(
                    concluida = !tarefa.concluida
                )
            )
        }
    }

    fun atualizarTarefa(tarefa: Tarefa) {
        viewModelScope.launch {
            repository.atualizar(tarefa)
        }
    }
}