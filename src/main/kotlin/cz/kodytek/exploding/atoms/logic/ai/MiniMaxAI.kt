package cz.kodytek.exploding.atoms.logic.ai

import cz.kodytek.exploding.atoms.logic.ExplodingAtomsGame
import cz.kodytek.exploding.atoms.logic.IExplodingAtomsGame
import cz.kodytek.exploding.atoms.logic.models.Board
import cz.kodytek.exploding.atoms.logic.models.Coordinate
import cz.kodytek.exploding.atoms.logic.models.PlayerType

data class Result(val ratio: Float, val move: Coordinate)

class MiniMaxAI : IExplodingAtomsAI {
    override fun getName(): String = "MiniMax"

    override fun makeMove(currentGameState: IExplodingAtomsGame?, playerType: PlayerType?): Coordinate {
        val positions = getPossiblePositions(currentGameState!!.board, playerType!!).toMutableList()
        if (positions.isEmpty())
            positions += getAllPossiblePositions(currentGameState.board, playerType).random()

        return (positions).map { coordinate ->
            val simulatedMove = simulateMove(currentGameState, coordinate) ?: return@map null
            Result(miniMax(simulatedMove, playerType.togglePlayer(), playerType, 2, true), coordinate)
        }.filterNotNull().maxByOrNull { it.ratio }!!.move
    }

    private fun miniMax(
        currentGameState: IExplodingAtomsGame?,
        playerType: PlayerType,
        team: PlayerType,
        depth: Int,
        min: Boolean
    ): Float {
        val result = (getPossiblePositions(currentGameState!!.board, playerType)).map { coordinate ->
            val simulatedMove = simulateMove(currentGameState, coordinate) ?: return@map null

            if (depth > 0)
                return@map miniMax(simulatedMove, playerType.togglePlayer(), team, depth - 1, !min)
            else
                return@map simulatedMove.getRatio(team)
        }.filterNotNull()

        return if (min)
            result.minByOrNull { it } ?: 1f
        else
            result.maxByOrNull { it } ?: 0f
    }

    private fun getPossiblePositions(board: Board, playerType: PlayerType): List<Coordinate> {
        val positions: MutableList<Coordinate> = mutableListOf()
        for (i in 0..board.rowCount) {
            for (j in 0..board.columnCount) {
                if (board.getOrNull(i, j)?.owner == playerType)
                    positions.add(Coordinate(i, j))
            }
        }

        return positions
    }

    private fun getAllPossiblePositions(board: Board, playerType: PlayerType): List<Coordinate> {
        val positions: MutableList<Coordinate> = mutableListOf()
        for (i in 0..board.rowCount) {
            for (j in 0..board.columnCount) {
                if (board.getOrNull(i, j)?.owner != playerType.togglePlayer())
                    positions.add(Coordinate(i, j))
            }
        }

        return positions
    }

    private fun simulateMove(currentGameState: IExplodingAtomsGame, coordinate: Coordinate): ExplodingAtomsGame? {
        val game = ExplodingAtomsGame(currentGameState)
        return try {
            game.move(coordinate.x, coordinate.y)
            game
        } catch (e: Exception) {
            null
        }
    }
}