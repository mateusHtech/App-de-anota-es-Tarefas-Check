package BlocoDeNotas.trabalhofinaldesenvolvimentoandroidintermediario.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tarefas")
data class Tarefa(

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,

    val titulo: String,

    val detalhes: String,

    val concluida: Boolean = false,

    val cor: String = "LARANJA"
)