package fr.isen.toiletgame

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_power4.*
import android.widget.AdapterView.OnItemClickListener
import kotlin.math.max
import kotlin.math.min


private const val VOID = 0
private const val RED = 1
private const val YELLOW = 2

class Power4Activity : AppCompatActivity() {

    //Tableau 2D représentant le terrain de jeu
    private var power4 =
        arrayOf(arrayOf(0,0,0,0,0,0),
            arrayOf(0,0,0,0,0,0),
            arrayOf(0,0,0,0,0,0),
            arrayOf(0,0,0,0,0,0),
            arrayOf(0,0,0,0,0,0),
            arrayOf(0,0,0,0,0,0),
            arrayOf(0,0,0,0,0,0)
        )


    //Si il y a l'IA
    private var IA = true
    //Si il y a 2 joueurs, Alterne entre RED et YELLOW
    private var joueurActuelle = RED
    //Empêche de jouer si true
    private var endGame = false

    //Pour gérer les listViews
    private val adapters = ArrayList<MyAdapter>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_power4)
        
        //Liste des adapters pour chaque listView
        adapters.add(MyAdapter(this, power4[0]))
        adapters.add(MyAdapter(this, power4[1]))
        adapters.add(MyAdapter(this, power4[2]))
        adapters.add(MyAdapter(this, power4[3]))
        adapters.add(MyAdapter(this, power4[4]))
        adapters.add(MyAdapter(this, power4[5]))
        adapters.add(MyAdapter(this, power4[6]))


        //setOnClickListener pour chaque listView
        column0.adapter = adapters[0]
        column0.onItemClickListener = OnItemClickListener { _, _, _, _ ->
            onPlay(0)
        }

        column1.adapter = adapters[1]
        column1.onItemClickListener = OnItemClickListener { _, _, _, _ ->
            onPlay(1)
        }
        
        column2.adapter = adapters[2]
        column2.onItemClickListener = OnItemClickListener { _, _, _, _ ->
            onPlay(2)
        }

        column3.adapter = adapters[3]
        column3.onItemClickListener = OnItemClickListener { _, _, _, _ ->
            onPlay(3)
        }

        column4.adapter = adapters[4]
        column4.onItemClickListener = OnItemClickListener { _, _, _, _ ->
            onPlay(4)
        }

        column5.adapter = adapters[5]
        column5.onItemClickListener = OnItemClickListener { _, _, _, _ ->
            onPlay(5)
        }


        column6.adapter = adapters[6]
        column6.onItemClickListener = OnItemClickListener { _, _, _, _ ->
            onPlay(6)
        }

        //Lorsque l'on clique sur le bouton nouvelle partie avec IA
        nouvellePartieIA.setOnClickListener{
            IA = true
            onNewGame()
        }

        //Lorsque l'on clique sur le bouton nouvelle partie avec un autre joueur
        nouvellePartieJoueur.setOnClickListener{
            IA = false
            onNewGame()
        }
    }

    //Fonction principal du jeu
    private fun onPlay(column: Int){

        val ligne = getFirstLigneVoid(column)
        if (ligne != null && !endGame){//Si la partie n'est pas finie et que la colonne que l'on a cliqué a un emplacement libre

            power4[column][ligne] = joueurActuelle//Un jeton est rajouté dans la colonne


            if (!checkWin(column,ligne)){//Si cela n'entraine pas la victoire
                if(IA){//Si la partie est face à l'IA
                    onPlayIA()//On fait joueur l'IA
                }else{//Si la partie est face à un autre joueur
                    //On échange de joueur
                    if(joueurActuelle == RED){
                        joueurActuelle = YELLOW
                    }else{
                        joueurActuelle = RED
                    }
                }
                if(checkWhereItIsPossibleToPlay().isEmpty()){//Si le terrain de jeu est plein
                    endGame = true//La partie est terminé
                    Toast.makeText(this, "Egalité", Toast.LENGTH_LONG).show()//Egalité
                }
            }else{//Si la victoire d'un joueur a lieu
                if(joueurActuelle == RED){
                    Toast.makeText(this, "RED Victory", Toast.LENGTH_LONG).show()
                }else{
                    Toast.makeText(this, "YELLOW Victory", Toast.LENGTH_LONG).show()
                }
                endGame = true
            }
        }
        //On met à jour les listView
        chechIfUpdate()
    }

    //On réinitialise les valeur
    private fun onNewGame(){
        for(i in 0..6) {
            for (j in 0..5) {
                power4[i][j] = VOID
                joueurActuelle = RED
                endGame = false
            }
        }
        chechIfUpdate()
    }

    //Met à jour les listView avec les nouvelles valeurs
    private fun chechIfUpdate(){
        for(adapter in adapters){
            adapter.notifyDataSetChanged()
        }
    }

    //Retourne la première ligne ne contenant pas de jeton dans la oolonne
    private fun getFirstLigneVoid(column : Int): Int?{
        for (i in 5 downTo 0) {//On parcourt les lignes du bas vers le haut
            if (power4[column][i] == VOID){//si il n'y a pas de jetons
                return i//Renvoie la ligne correspondante
            }
        }
        return null//Sinon ne retourne rien
    }

    //Fonction gérant le comportement de l'IA
    private fun onPlayIA(){
        var column = checkCanWin(YELLOW)//On vérifie si l'IA peut gagner
        if(column != null){//Si c'est le cas
            val ligne = getFirstLigneVoid(column)
            power4[column][ligne!!] = YELLOW//L'IA joue à l'emplacement
            endGame = true//La partie se termine par la défaite du joueur
            Toast.makeText(this, "You Lose", Toast.LENGTH_LONG).show()
            return//l'IA s'arrête ici
        }
        column = checkCanWin(RED)//On vérifie si le joueur peut gagner
        if (column != null) {//Si le joueur peut gagner
            val ligne = getFirstLigneVoid(column)
            power4[column][ligne!!] = YELLOW//L'IA empêche le joueur de gagner
            return//l'IA s'arrête ici
        }

        //Si l'IA ne peut pas gagner ou le joueur ne peut pas gagner au prochain tour
        //Elle vérifie si en placant dans une colonne, le joueur peut gagner au prochain tour
        val whereICanPlayWithoutLosing = checkWhereCanPlayWithoutLosing()
        if(whereICanPlayWithoutLosing.isEmpty()){//Si l'IA peut jouer n'importe où sans avoir peur de perdre
            column = checkWhereItIsPossibleToPlay().random()//Elle joue aléatoirement où elle peut
            val ligne = getFirstLigneVoid(column)
            power4[column][ligne!!] = YELLOW
        }else{//Si l'IA ne peut pas jouer n'importe où sans avoir peur de perdre
            column = whereICanPlayWithoutLosing.random()//Elle joue aléatoirement où elle peut sans perdre
            val ligne = getFirstLigneVoid(column)
            power4[column][ligne!!] = YELLOW
        }        
    }


    //Fonction retournant les colonnes où il est possible de jouer dans le terrain de jeu
    private fun checkWhereItIsPossibleToPlay():ArrayList<Int>{
        val tableau = ArrayList<Int>()
        for (i in 0..6){//On vérifie les colonnes
            val ligne = getFirstLigneVoid(i)
            if (ligne != null){//Si la colonne a un emplacement vide
                tableau.add(i)//on l'a rajoute au tableau qui sera renvoyé
            }
        }
        return tableau
    }

    //Vérifie si une couleur peut gagner quelque part
    private fun checkCanWin(color: Int): Int?{
        for(column in 0..6){//on vérifie chaque colonne
            val ligne = getFirstLigneVoid(column)
            if (ligne != null){//Si la colonne a un emplacement vide
                power4[column][ligne] = color//On met une variable temporaire
                if(checkWin(column,ligne)){//Si cet emplacement permet la victoire
                    power4[column][ligne] = VOID//On enlève la variable temporaire
                    return column//on retourne la colonne permettant la victoire
                }else{//Si cet emplacement ne permet pas la victoire
                    power4[column][ligne] = VOID//On enlève la variable temporaire
                }
            }
        }
        return null//Si aucune colonne ne permet la victoire, on ne renvoit rien
    }

    //Fonction retournant les colonnes où il est possible de jouer dans le terrain de jeu sans perdre au prochain tour
    private fun checkWhereCanPlayWithoutLosing() : ArrayList<Int>{
        val tableau = ArrayList<Int>()
        for (i in 0..6){//on vérifie chaque colonne
            val ligne = getFirstLigneVoid(i)
            if (ligne != null){//Si la colonne a un emplacement vide
                power4[i][ligne] = YELLOW//On met une variable temporaire pour simuler un tour
                val column = checkCanWin(RED)//On vérifie si le joueur peut gagner
                if(column == null){//Si le joueur ne peut pas gagner avec cet variable temporaire
                    tableau.add(i)//on rajoute la colonne au tableau qui sera renvoyé
                }
                power4[i][ligne] = VOID//On enlève la variable temporaire
            }
        }
        return tableau
    }

    //Vérifie si l'emplacement donne la victoire
    private fun checkWin(column: Int,ligne: Int): Boolean{
        //On n'a besoin que des jetons se trouvant à une distance de 3 de l'emplacement et qui se trouve dans le tableau
        val minC = max(column-3,0)
        val maxC = min(column+3,6)
        val minL = max(ligne-3,0)
        val maxL = min(ligne+3,5)

        //vérifie les conditions de victoire pour les différentes directions
        val numbers: IntArray = intArrayOf(0, 0, 0, 0)//Ligne,Colonne,Diagonale Haut-Bas, Diagonale Bas-Haut

        for(i in minC..maxC){//pour les colonnes à proximité

            if(power4[i][ligne] == power4[column][ligne]){//On check sur la ligne le jeton
                numbers[0] ++
            }else{//Si le jeton n'est pas bon
                if(numbers[0]<4){//Si on n'a pas déjà gagné
                    numbers[0] = 0
                }
            }
        }

        for (i in -3..3){//Pour calculer les diagonales
            if(checkIfInside(column+i,ligne+i)) {//On vérifie si les valeurs sont à l'intérieur du tableau
                if (power4[column + i][ligne + i] == power4[column][ligne]) {//On check sur la diagonale Haut-Bas le jeton
                    numbers[2]++
                } else {//Si le jeton n'est pas bon
                    if (numbers[2] < 4) {//Si on n'a pas déjà gagné
                        numbers[2] = 0
                    }
                }
            }
            if(checkIfInside(column+i,ligne-i)) {//On vérifie si les valeurs sont à l'intérieur du tableau
                if (power4[column + i][ligne - i] == power4[column][ligne]) {//On check sur la diagonale Bas-Haut le jeton
                    numbers[3]++
                } else {//Si le jeton n'est pas bon
                    if (numbers[3] < 4) {//Si on n'a pas déjà gagné
                        numbers[3] = 0
                    }
                }
            }
        }


        for(i in minL..maxL){//pour les lignes à proximité
            if(power4[column][i] == power4[column][ligne]){//On check sur la colonne le jeton
                numbers[1] ++
            }else{//Si le jeton n'est pas bon
                if(numbers[1] < 4){//Si on n'a pas déjà gagné
                    numbers[1] = 0
                }
            }
        }

        if(numbers.filter{it > 3 }.isNotEmpty()){//Si on a 4 jetons d'affilé dans une des directions
            return true//On dit que cet emplacement fait gagné
        }
        return false//On dit que cet emplacement ne fait pas gagné
    }


    //Vérifie si l'emplacement se trouve dans le terrain de jeu
    private fun checkIfInside(column: Int, ligne: Int):Boolean{
        return (column < power4.size && column >= 0 && ligne < power4[0].size && ligne >= 0 )
    }


}
