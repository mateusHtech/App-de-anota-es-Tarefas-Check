package BlocoDeNotas.trabalhofinaldesenvolvimentoandroidintermediario

import android.os.Bundle

import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width

import androidx.compose.foundation.shape.RoundedCornerShape

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Menu

import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.OutlinedTextField

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateListOf

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

import BlocoDeNotas.trabalhofinaldesenvolvimentoandroidintermediario.data.Tarefa
import BlocoDeNotas.trabalhofinaldesenvolvimentoandroidintermediario.data.TarefaDatabase
import BlocoDeNotas.trabalhofinaldesenvolvimentoandroidintermediario.data.TarefaRepository

import BlocoDeNotas.trabalhofinaldesenvolvimentoandroidintermediario.ui.theme.TrabalhoFinalDesenvolvimentoAndroidIntermediarioTheme

import BlocoDeNotas.trabalhofinaldesenvolvimentoandroidintermediario.viewmodel.TarefaViewModel
import BlocoDeNotas.trabalhofinaldesenvolvimentoandroidintermediario.viewmodel.TarefaViewModelFactory

import androidx.compose.animation.animateContentSize
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.ui.text.style.TextDecoration

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.ImeAction


import BlocoDeNotas.trabalhofinaldesenvolvimentoandroidintermediario.data.CorTarefa
import BlocoDeNotas.trabalhofinaldesenvolvimentoandroidintermediario.ui.theme.FundoTarefasCheck


import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.layout.size

import androidx.compose.ui.draw.clip

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

import androidx.compose.foundation.isSystemInDarkTheme

import androidx.compose.material3.RadioButton

import androidx.compose.material3.TextButton

import androidx.compose.material3.MaterialTheme

enum class FiltroTarefa {
    TODAS,
    PENDENTES,
    CONCLUIDAS
}

fun corDaTarefa(cor: String): Color {
    return when (cor) {
        CorTarefa.LARANJA.valor -> Color(0xFFFFB74D)
        CorTarefa.AMARELO.valor -> Color(0xFFFFE082)
        CorTarefa.VERDE.valor -> Color(0xFFA5D6A7)
        CorTarefa.AZUL.valor -> Color(0xFF90CAF9)
        CorTarefa.ROXO.valor -> Color(0xFFCE93D8)
        CorTarefa.ROSA.valor -> Color(0xFFF48FB1)
        else -> Color(0xFFFFB74D)
    }
}

class MainActivity : ComponentActivity() {

    private val tarefaViewModel: TarefaViewModel by viewModels {
        val database = TarefaDatabase.obterBanco(applicationContext)
        val repository = TarefaRepository(database.tarefaDao())

        TarefaViewModelFactory(repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val preferencias = getSharedPreferences(
            "tarefas_check_preferencias",
            MODE_PRIVATE
        )

        setContent {

            var modoTema by remember {
                mutableStateOf(
                    preferencias.getString("modo_tema", "SISTEMA") ?: "SISTEMA"
                )
            }

            val temaEscuro = when (modoTema) {
                "ESCURO" -> true
                "CLARO" -> false
                else -> isSystemInDarkTheme()
            }

            TrabalhoFinalDesenvolvimentoAndroidIntermediarioTheme(
                darkTheme = temaEscuro,
                dynamicColor = false
            ) {

                TarefasCheckApp(
                    tarefaViewModel = tarefaViewModel,
                    modoTema = modoTema,
                    onMudarTema = { novoModo ->

                        modoTema = novoModo

                        preferencias.edit()
                            .putString("modo_tema", novoModo)
                            .apply()
                    }
                )
            }
        }
    }
}



@Composable
fun TarefasCheckApp(
    tarefaViewModel: TarefaViewModel,
    modoTema: String,
    onMudarTema: (String) -> Unit
) {

    val tarefas by tarefaViewModel.tarefas.collectAsState()

    val tarefasSelecionadas = remember {
        mutableStateListOf<Tarefa>()
    }

    val navController = rememberNavController()

    var tarefaSelecionada by remember {
        mutableStateOf<Tarefa?>(null)
    }

    var mostrarConfirmacaoExcluirTodas by remember {
        mutableStateOf(false)
    }

    var tarefaMenuAberto by remember { mutableStateOf<Tarefa?>(null) }

    var tarefaParaExcluir by remember {
        mutableStateOf<Tarefa?>(null)
    }

    NavHost(
        navController = navController,
        startDestination = "principal"
    ) {

        composable("principal") {

            TelaPrincipal(
                tarefas = tarefas,
                tarefasSelecionadas = tarefasSelecionadas,

                onCriarTarefa = {
                    navController.navigate("nova_tarefa")
                },

                onSelecionarTarefa = { tarefa ->

                    if (tarefasSelecionadas.contains(tarefa)) {
                        tarefasSelecionadas.remove(tarefa)
                    } else {
                        tarefasSelecionadas.add(tarefa)
                    }
                },
                onExcluirTodas = {
                    mostrarConfirmacaoExcluirTodas = true
                },
                onExcluirSelecionadas = {

                    tarefasSelecionadas.forEach { tarefa ->
                        tarefaViewModel.excluirTarefa(tarefa)
                    }

                    tarefasSelecionadas.clear()
                },
                onAbrirMenuTarefa = { tarefa ->
                    tarefaMenuAberto = tarefa
                },

                onAbrirMenuPrincipal = {
                    navController.navigate("menu")
                },

                onConcluirTarefa = { tarefa ->
                    tarefaViewModel.concluirTarefa(tarefa)
                },

                onAbrirDetalhes = { tarefa ->

                    tarefaSelecionada = tarefa

                    navController.navigate("detalhes")
                }
            )
        }

        composable("nova_tarefa") {
            TelaNovaTarefa(
                onVoltar = {
                    navController.popBackStack()
                },
                onSalvar = { titulo, detalhes, cor ->
                    tarefaViewModel.adicionarTarefa(
                        titulo = titulo,
                        detalhes = detalhes,
                        cor = cor
                    )

                    navController.popBackStack()
                }
            )
        }

        composable("editar_tarefa") {

            tarefaSelecionada?.let { tarefa ->

                TelaEditarTarefa(
                    tarefa = tarefa,

                    onVoltar = {
                        navController.popBackStack()
                    },

                    onSalvar = { tarefaEditada ->

                        tarefaViewModel.atualizarTarefa(tarefaEditada)

                        tarefaSelecionada = tarefaEditada

                        navController.popBackStack()
                    }
                )
            }
        }
        composable("detalhes") {

            tarefaSelecionada?.let { tarefa ->

                TelaDetalhes(
                    tarefa = tarefa,

                    onVoltar = {
                        navController.popBackStack()
                    },

                    onEditar = {
                        navController.navigate("editar_tarefa")
                    }
                )
            }
        }

        composable("menu") {
            TelaMenu(
                onVoltar = {
                    navController.popBackStack()
                },
                onConfiguracoes = {
                    navController.navigate("configuracoes")
                },
                onAjuda = {
                    navController.navigate("ajuda")
                },
                onSobre = {
                    navController.navigate("sobre")
                }
            )
        }

        composable("configuracoes") {
            TelaConfiguracoes(
                onVoltar = {
                    navController.popBackStack()
                },
                modoTema = modoTema,
                onMudarTema = onMudarTema,
                onAjuda = {
                    navController.navigate("ajuda")
                },
                onSobre = {
                    navController.navigate("sobre")
                }
            )
        }

        composable("ajuda") {
            TelaAjuda(
                onVoltar = {
                    navController.popBackStack()
                }
            )
        }

        composable("sobre") {
            TelaSobre(
                onVoltar = {
                    navController.popBackStack()
                }
            )
        }

    }
    if (tarefaMenuAberto != null) {

        val tarefa = tarefaMenuAberto!!

        DropdownMenu(
            expanded = true,
            onDismissRequest = {
                tarefaMenuAberto = null
            }
        ) {

            DropdownMenuItem(
                text = {
                    Text("Ver detalhes")
                },
                onClick = {
                    tarefaSelecionada = tarefa
                    tarefaMenuAberto = null
                    navController.navigate("detalhes")
                }
            )

            DropdownMenuItem(
                text = {
                    Text("Editar tarefa")
                },
                onClick = {
                    tarefaSelecionada = tarefa
                    tarefaMenuAberto = null
                    navController.navigate("editar_tarefa")
                }
            )

            DropdownMenuItem(
                text = {
                    Text("Excluir tarefa")
                },
                onClick = {
                    tarefaParaExcluir = tarefa
                    tarefaMenuAberto = null
                }
            )
        }
    }

    if (mostrarConfirmacaoExcluirTodas) {

        AlertDialog(
            onDismissRequest = {
                mostrarConfirmacaoExcluirTodas = false
            },

            title = {
                Text("Excluir todas as tarefas?")
            },

            text = {
                Text(
                    "Todas as tarefas serão removidas permanentemente."
                )
            },

            confirmButton = {

                TextButton(
                    onClick = {

                        tarefaViewModel.excluirTodas()
                        tarefasSelecionadas.clear()

                        mostrarConfirmacaoExcluirTodas = false
                    }
                ) {
                    Text("Excluir")
                }
            },

            dismissButton = {

                TextButton(
                    onClick = {
                        mostrarConfirmacaoExcluirTodas = false
                    }
                ) {
                    Text("Cancelar")
                }
            }
        )
    }
    if (tarefaParaExcluir != null) {

        AlertDialog(
            onDismissRequest = {
                tarefaParaExcluir = null
            },
            title = {
                Text("Excluir tarefa?")
            },
            text = {
                Text(
                    "Tem certeza que deseja excluir " +
                            "\"${tarefaParaExcluir?.titulo}\"?"
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        tarefaParaExcluir?.let { tarefa ->
                            tarefaViewModel.excluirTarefa(tarefa)
                        }

                        tarefaParaExcluir = null
                    }
                ) {
                    Text("Excluir")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        tarefaParaExcluir = null
                    }
                ) {
                    Text("Cancelar")
                }
            }
        )
    }
}




    @Composable
    fun TelaMenu(
        onVoltar: () -> Unit,
        onConfiguracoes: () -> Unit,
        onAjuda: () -> Unit,
        onSobre: () -> Unit
    ) {
        val fundo = FundoTarefasCheck()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(fundo)
                .padding(16.dp)
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onVoltar) {
                    Text(
                        text = "←",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Text(
                    text = "Menu",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "Tarefas-Check",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Organize suas tarefas do seu jeito.",
                fontSize = 15.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(32.dp))

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(70.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface.copy(
                        alpha = 0.85f
                    )
                ),
                onClick = onConfiguracoes
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("⚙", fontSize = 26.sp)

                    Spacer(modifier = Modifier.width(14.dp))

                    Column {
                        Text(
                            text = "Configurações",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = "Personalize o aplicativo",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(70.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface.copy(
                        alpha = 0.85f
                    )
                ),
                onClick = onAjuda
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("❔", fontSize = 26.sp)

                    Spacer(modifier = Modifier.width(14.dp))

                    Column {
                        Text(
                            text = "Ajuda e instruções",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = "Aprenda a utilizar o aplicativo",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(70.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface.copy(
                        alpha = 0.85f
                    )
                ),
                onClick = onSobre
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("ⓘ", fontSize = 26.sp)

                    Spacer(modifier = Modifier.width(14.dp))

                    Column {
                        Text(
                            text = "Sobre o aplicativo",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = "Conheça o projeto e suas tecnologias",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }

    @Composable
    fun TelaPrincipal(
        tarefas: List<Tarefa>,
        tarefasSelecionadas: List<Tarefa>,
        onCriarTarefa: () -> Unit,
        onSelecionarTarefa: (Tarefa) -> Unit,
        onExcluirSelecionadas: () -> Unit,
        onExcluirTodas: () -> Unit,
        onConcluirTarefa: (Tarefa) -> Unit,
        onAbrirDetalhes: (Tarefa) -> Unit,
        onAbrirMenuTarefa: (Tarefa) -> Unit,
        onAbrirMenuPrincipal: () -> Unit
    ) {

        var filtroSelecionado by remember {
            mutableStateOf(FiltroTarefa.TODAS)
        }

        var menuFiltroAberto by remember {
            mutableStateOf(false)
        }

        val tarefasFiltradas = when (filtroSelecionado) {

            FiltroTarefa.TODAS -> tarefas

            FiltroTarefa.PENDENTES ->
                tarefas.filter { !it.concluida }

            FiltroTarefa.CONCLUIDAS ->
                tarefas.filter { it.concluida }
        }

        val fundo = FundoTarefasCheck()

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(fundo)
                .padding(16.dp)
        ) {

            Column(
                modifier = Modifier.fillMaxSize()
            ) {

                // CABEÇALHO
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    // MENU PRINCIPAL
                    IconButton(
                        onClick = onAbrirMenuPrincipal
                    ) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "Menu"
                        )
                    }


                    // FILTROS
                    Box {

                        IconButton(
                            onClick = {
                                menuFiltroAberto = true
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.GridView,
                                contentDescription = "Filtros"
                            )
                        }

                        DropdownMenu(
                            expanded = menuFiltroAberto,
                            onDismissRequest = {
                                menuFiltroAberto = false
                            }
                        ) {

                            DropdownMenuItem(
                                text = {
                                    Text("Todas")
                                },
                                onClick = {
                                    filtroSelecionado = FiltroTarefa.TODAS
                                    menuFiltroAberto = false
                                }
                            )

                            DropdownMenuItem(
                                text = {
                                    Text("Pendentes")
                                },
                                onClick = {
                                    filtroSelecionado = FiltroTarefa.PENDENTES
                                    menuFiltroAberto = false
                                }
                            )

                            DropdownMenuItem(
                                text = {
                                    Text("Concluídas")
                                },
                                onClick = {
                                    filtroSelecionado = FiltroTarefa.CONCLUIDAS
                                    menuFiltroAberto = false
                                }
                            )
                        }
                    }

                    Spacer(
                        modifier = Modifier.weight(1f)
                    )

                    Text(
                        text = "Tarefas-Check",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onBackground
                    )
                }

                if (tarefasSelecionadas.isNotEmpty()) {

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(55.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        ),
                        onClick = onExcluirSelecionadas
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Excluir selecionadas (${tarefasSelecionadas.size})",
                                color = MaterialTheme.colorScheme.onPrimary,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                }

                // ÁREA CENTRAL
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {

                    if (tarefas.isEmpty()) {

                        Text(
                            text = "📋",
                            fontSize = 48.sp
                        )

                        Spacer(
                            modifier = Modifier.height(16.dp)
                        )

                        Text(
                            text = "Bem vindo! Vamos organizar\nsuas atividades?",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground
                        )

                    } else {

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .animateContentSize()
                        ) {

                            Text(
                                text = when (filtroSelecionado) {
                                    FiltroTarefa.TODAS -> "Suas tarefas"
                                    FiltroTarefa.PENDENTES -> "Tarefas pendentes"
                                    FiltroTarefa.CONCLUIDAS -> "Tarefas concluídas"
                                },
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold
                            )

                            Spacer(
                                modifier = Modifier.height(16.dp)
                            )

                            if (tarefasFiltradas.isEmpty()) {

                                Text(
                                    text = "Nenhuma tarefa encontrada.",
                                    fontSize = 16.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )

                            } else {

                                tarefasFiltradas.forEach { tarefa ->

                                    Card(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(bottom = 12.dp)
                                            .animateContentSize(),
                                        shape = RoundedCornerShape(16.dp),
                                        colors = CardDefaults.cardColors(
                                            containerColor = corDaTarefa(tarefa.cor),
                                            contentColor = Color(0xFF29201B)
                                        )

                                    ){

                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(12.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {

                                            // SELECIONAR TAREFA
                                            Checkbox(
                                                checked = tarefasSelecionadas.contains(tarefa),
                                                onCheckedChange = {
                                                    onSelecionarTarefa(tarefa)
                                                }
                                            )

                                            // TÍTULO E DETALHES
                                            Column(
                                                modifier = Modifier.weight(1f)
                                            ) {

                                                Text(
                                                    text = tarefa.titulo,
                                                    fontSize = 17.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    textDecoration = if (tarefa.concluida) {
                                                        TextDecoration.LineThrough
                                                    } else {
                                                        TextDecoration.None
                                                    }
                                                )

                                                Spacer(
                                                    modifier = Modifier.height(6.dp)
                                                )

                                                Text(
                                                    text = tarefa.detalhes,
                                                    fontSize = 14.sp,
                                                    color = Color(0xFF5A4030),
                                                    textDecoration = if (tarefa.concluida) {
                                                        TextDecoration.LineThrough
                                                    } else {
                                                        TextDecoration.None
                                                    }
                                                )
                                            }

                                            // BOTÃO DETALHES
                                            IconButton(
                                                onClick = {
                                                    onAbrirMenuTarefa(tarefa)
                                                }
                                            ) {
                                                Text(
                                                    text = "⋮",
                                                    fontSize = 24.sp,
                                                    fontWeight = FontWeight.Bold
                                                )
                                            }

                                            // BOTÃO CONCLUIR
                                            IconButton(
                                                onClick = {
                                                    onConcluirTarefa(tarefa)
                                                }
                                            ) {
                                                Text(
                                                    text = if (tarefa.concluida) {
                                                        "✓"
                                                    } else {
                                                        "○"
                                                    },
                                                    fontSize = 24.sp,
                                                    fontWeight = FontWeight.Bold
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                // EXCLUIR TODAS
                if (tarefas.isNotEmpty()) {

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(55.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        ),
                        onClick = onExcluirTodas
                    ) {

                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Excluir todas",
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    }

                    Spacer(
                        modifier = Modifier.height(12.dp)
                    )
                }

                // NOVA TAREFA
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(68.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface.copy(
                            alpha = 0.85f
                        )
                    ),
                    onClick = onCriarTarefa
                ) {

                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .background(
                                    color = MaterialTheme.colorScheme.primary,
                                    shape = RoundedCornerShape(12.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Adicionar",
                                tint = MaterialTheme.colorScheme.onPrimary
                            )
                        }

                        Spacer(
                            modifier = Modifier.width(12.dp)
                        )

                        Column {

                            Text(
                                text = "Criar uma nova tarefa",
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp
                            )

                            Text(
                                text = "Adicione título e detalhes",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
                Spacer(
                    modifier = Modifier.height(12.dp)
                )
            }
        }
    }


    @Composable
    fun TelaNovaTarefa(
        onVoltar: () -> Unit,
        onSalvar: (String, String, String) -> Unit
    ) {
        var titulo by remember {
            mutableStateOf("")
        }

        var detalhes by remember {
            mutableStateOf("")
        }

        var corSelecionada by remember {
            mutableStateOf(CorTarefa.LARANJA.valor)
        }

        var erroTitulo by remember {
            mutableStateOf(false)
        }

        val fundo = FundoTarefasCheck()

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(fundo)
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onVoltar) {
                        Text(
                            text = "←",
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Text(
                        text = "Nova tarefa",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

                Text(
                    text = "Título *",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = titulo,
                    onValueChange = {
                        titulo = it
                        if (it.isNotBlank()) erroTitulo = false
                    },
                    label = { Text("Título") },
                    isError = erroTitulo,
                    supportingText = {
                        if (erroTitulo) {
                            Text("Digite um título para a tarefa.")
                        }
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "Detalhes",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = detalhes,
                    onValueChange = { detalhes = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp),
                    placeholder = {
                        Text("Descreva sua tarefa...")
                    },
                    shape = RoundedCornerShape(16.dp),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Done
                    )
                )

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "Cor da tarefa",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(12.dp))

                SeletorCores(
                    corSelecionada = corSelecionada,
                    onSelecionarCor = { corSelecionada = it }
                )

                Spacer(modifier = Modifier.weight(1f))

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(65.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    ),
                    onClick = {
                        if (titulo.isBlank()) {
                            erroTitulo = true
                        } else {
                            onSalvar(
                                titulo.trim(),
                                detalhes.trim(),
                                corSelecionada
                            )
                        }
                    }
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Salvar tarefa",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
            }
        }
    }

    @Composable
    fun CorBotao(
        cor: Color,
        selecionada: Boolean,
        onClick: () -> Unit
    ) {
        Box(
            modifier = Modifier
                .size(42.dp)
                .clip(CircleShape)
                .background(cor)
                .border(
                    width = if (selecionada) 3.dp else 1.dp,
                    color = if (selecionada) Color.Black else Color.White,
                    shape = CircleShape
                )
                .clickable {
                    onClick()
                },
            contentAlignment = Alignment.Center
        ) {
            if (selecionada) {
                Text(
                    text = "✓",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            }
        }
    }

    @Composable
    fun TelaDetalhes(
        tarefa: Tarefa,
        onVoltar: () -> Unit,
        onEditar: () -> Unit
    ) {
        val fundo = FundoTarefasCheck()
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(fundo)
                .padding(16.dp)
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                IconButton(
                    onClick = onVoltar
                ) {
                    Text(
                        text = "←",
                        fontSize = 28.sp
                    )
                }

                Text(
                    text = "Detalhes",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = tarefa.titulo,
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = tarefa.detalhes,
                fontSize = 16.sp
            )

            Spacer(modifier = Modifier.weight(1f))

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primary
                ),
                onClick = onEditar
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Editar tarefa",
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }

    @Composable
    fun TelaConfiguracoes(
        onVoltar: () -> Unit,
        modoTema: String,
        onMudarTema: (String) -> Unit,
        onAjuda: () -> Unit,
        onSobre: () -> Unit
    ) {
        val fundo = FundoTarefasCheck()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(fundo)
                .padding(16.dp)
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onVoltar) {
                    Text("←", fontSize = 28.sp)
                }

                Text(
                    text = "Configurações",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(28.dp))

            Text(
                text = "Aparência",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(12.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface.copy(
                        alpha = 0.85f
                    )
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Tema do aplicativo",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    val temas = listOf(
                        "CLARO" to "☀ Claro",
                        "ESCURO" to "🌙 Escuro",
                        "SISTEMA" to "📱 Automático"
                    )

                    temas.forEach { (valor, nome) ->

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    onMudarTema(valor)
                                }
                                .padding(vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = modoTema == valor,
                                onClick = {
                                    onMudarTema(valor)
                                }
                            )

                            Spacer(modifier = Modifier.width(8.dp))

                            Text(
                                text = nome,
                                fontSize = 15.sp
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Informações",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(12.dp))

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = onAjuda),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface.copy(
                        alpha = 0.85f
                    )
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Ajuda e instruções",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )

                    Text(
                        text = "Aprenda a utilizar as ferramentas do aplicativo.",
                        fontSize = 13.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = onSobre),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface.copy(
                        alpha = 0.85f
                    )
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Sobre o aplicativo",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )

                    Text(
                        text = "Informações sobre o Tarefas-Check.",
                        fontSize = 13.sp
                    )
                }
            }
        }
    }

    @Composable
    fun TelaEditarTarefa(
        tarefa: Tarefa,
        onVoltar: () -> Unit,
        onSalvar: (Tarefa) -> Unit
    ) {
        var titulo by remember(tarefa.id) {
            mutableStateOf(tarefa.titulo)
        }

        var detalhes by remember(tarefa.id) {
            mutableStateOf(tarefa.detalhes)
        }

        var corSelecionada by remember(tarefa.id) {
            mutableStateOf(tarefa.cor)
        }

        var erroTitulo by remember {
            mutableStateOf(false)
        }

        val fundo = FundoTarefasCheck()

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(fundo)
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onVoltar) {
                        Text(
                            text = "←",
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Text(
                        text = "Editar tarefa",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

                Text(
                    text = "Título *",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = titulo,
                    onValueChange = {
                        titulo = it
                        if (it.isNotBlank()) erroTitulo = false
                    },
                    label = { Text("Título") },
                    isError = erroTitulo,
                    supportingText = {
                        if (erroTitulo) {
                            Text("Digite um título para a tarefa.")
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "Detalhes",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = detalhes,
                    onValueChange = { detalhes = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp),
                    placeholder = {
                        Text("Descreva sua tarefa...")
                    },
                    shape = RoundedCornerShape(16.dp)
                )

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "Cor da tarefa",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(12.dp))

                SeletorCores(
                    corSelecionada = corSelecionada,
                    onSelecionarCor = { corSelecionada = it }
                )

                Spacer(modifier = Modifier.weight(1f))

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(65.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    ),
                    onClick = {
                        if (titulo.isBlank()) {
                            erroTitulo = true
                        } else {
                            onSalvar(
                                tarefa.copy(
                                    titulo = titulo.trim(),
                                    detalhes = detalhes.trim(),
                                    cor = corSelecionada
                                )
                            )
                        }
                    }
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Salvar alterações",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
            }
        }
    }

    @Composable
    fun SeletorCores(
        corSelecionada: String,
        onSelecionarCor: (String) -> Unit
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            CorBotao(
                cor = Color(0xFFFFB74D),
                selecionada = corSelecionada == CorTarefa.LARANJA.valor,
                onClick = {
                    onSelecionarCor(CorTarefa.LARANJA.valor)
                }
            )

            CorBotao(
                cor = Color(0xFFFFE082),
                selecionada = corSelecionada == CorTarefa.AMARELO.valor,
                onClick = {
                    onSelecionarCor(CorTarefa.AMARELO.valor)
                }
            )

            CorBotao(
                cor = Color(0xFFA5D6A7),
                selecionada = corSelecionada == CorTarefa.VERDE.valor,
                onClick = {
                    onSelecionarCor(CorTarefa.VERDE.valor)
                }
            )

            CorBotao(
                cor = Color(0xFF90CAF9),
                selecionada = corSelecionada == CorTarefa.AZUL.valor,
                onClick = {
                    onSelecionarCor(CorTarefa.AZUL.valor)
                }
            )

            CorBotao(
                cor = Color(0xFFCE93D8),
                selecionada = corSelecionada == CorTarefa.ROXO.valor,
                onClick = {
                    onSelecionarCor(CorTarefa.ROXO.valor)
                }
            )

            CorBotao(
                cor = Color(0xFFF48FB1),
                selecionada = corSelecionada == CorTarefa.ROSA.valor,
                onClick = {
                    onSelecionarCor(CorTarefa.ROSA.valor)
                }
            )
        }
    }

    @Composable
    fun TelaAjuda(
        onVoltar: () -> Unit
    ) {
        val fundo = FundoTarefasCheck()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(fundo)
                .padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onVoltar) {
                    Text("←", fontSize = 28.sp)
                }

                Text(
                    text = "Ajuda e instruções",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Como utilizar o Tarefas-Check",
                fontSize = 19.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = """
                1. Criar tarefas

                Na tela principal, selecione "Criar uma nova tarefa".
                Informe um título, adicione detalhes e escolha uma cor.
                Depois, salve sua tarefa.

                2. Visualizar detalhes

                Toque no menu de três pontos de uma tarefa e escolha
                "Ver detalhes".

                3. Editar tarefas

                No menu de três pontos, escolha "Editar tarefa".
                Altere as informações e salve.

                4. Concluir tarefas

                Utilize o botão de conclusão ao lado da tarefa.
                Tarefas concluídas aparecem riscadas.

                5. Selecionar e excluir

                Marque as caixas de seleção das tarefas desejadas.
                Depois, utilize a opção de excluir selecionadas.
                Também é possível excluir todas as tarefas.

                6. Filtrar tarefas

                Utilize o botão de filtros na tela principal para
                visualizar todas, pendentes ou concluídas.

                7. Personalizar o aplicativo

                Acesse Menu > Configurações para escolher entre
                tema claro, escuro ou automático.
            """.trimIndent(),
                fontSize = 14.sp,
                lineHeight = 21.sp
            )
        }
    }

    @Composable
    fun TelaSobre(
        onVoltar: () -> Unit
    ) {
        val fundo = FundoTarefasCheck()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(fundo)
                .padding(16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onVoltar) {
                    Text("←", fontSize = 28.sp)
                }

                Text(
                    text = "Sobre o aplicativo",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(40.dp))

            Text("📋", fontSize = 64.sp)

            Text(
                text = "Tarefas-Check",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )

            Text("Versão 1.0.0", fontSize = 14.sp)

            Spacer(modifier = Modifier.height(28.dp))

            Text(
                text = "Sobre o projeto",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "O Aplicativo ainda está em desenvolvimento, futuras atualizações estão chegando. " +
                        "Mas aqui está o projeto acadêmico do curso de Desenvolvimento " +
                        "Android Intermediário.",
                fontSize = 15.sp,
                lineHeight = 22.sp
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Tecnologias utilizadas",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = """
                • Kotlin
                • Jetpack Compose
                • Room Database
                • MVVM
                • Navigation Compose
            """.trimIndent(),
                fontSize = 15.sp,
                lineHeight = 24.sp
            )
        }
    }


