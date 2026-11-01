package BlocoDeNotas.trabalhofinaldesenvolvimentoandroidintermediario.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface TarefaDao {

    @Query("SELECT * FROM tarefas ORDER BY id DESC")
    fun listarTarefas(): Flow<List<Tarefa>>

    @Insert
    suspend fun inserir(tarefa: Tarefa): Long

    @Update
    suspend fun atualizar(tarefa: Tarefa)

    @Delete
    suspend fun excluir(tarefa: Tarefa)

    @Query("DELETE FROM tarefas")
    suspend fun excluirTodas()

    @Query("DELETE FROM tarefas WHERE id = :id")
    suspend fun excluirPorId(id: Long)
}
