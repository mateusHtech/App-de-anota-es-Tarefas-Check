package BlocoDeNotas.trabalhofinaldesenvolvimentoandroidintermediario.data

import kotlinx.coroutines.flow.Flow

class TarefaRepository(
    private val tarefaDao: TarefaDao
) {
    val tarefas: Flow<List<Tarefa>> =
        tarefaDao.listarTarefas()

    suspend fun inserir(tarefa: Tarefa) {
        tarefaDao.inserir(tarefa)
    }

    suspend fun atualizar(tarefa: Tarefa) {
        tarefaDao.atualizar(tarefa)
    }

    suspend fun excluir(tarefa: Tarefa) {
        tarefaDao.excluir(tarefa)
    }

    suspend fun excluirTodas() {
        tarefaDao.excluirTodas()
    }

    suspend fun excluirPorId(id: Long) {
        tarefaDao.excluirPorId(id)
    }
}
