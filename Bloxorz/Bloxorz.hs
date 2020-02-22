{-# OPTIONS_GHC -Wall #-}
{-# LANGUAGE EmptyDataDecls, MultiParamTypeClasses,
             TypeSynonymInstances, FlexibleInstances,
             InstanceSigs #-}


module Bloxorz where

import ProblemState

import qualified Data.Array as A
import Data.List

{-
    Caracterele ce vor fi printate pentru fiecare tip de obiect din joc 
    Puteți înlocui aceste caractere cu orice, în afară de '\n'.
-}

hardTile :: Char
hardTile = '▒'

softTile :: Char
softTile = '='

block :: Char
block = '▓'

switch :: Char
switch = '±'

emptySpace :: Char
emptySpace = ' '

winningTile :: Char
winningTile = '*'

{-
    Sinonim de tip de date pentru reprezetarea unei perechi (int, int)
    care va reține coordonatele de pe tabla jocului
-}

type Position = (Int, Int)

{-
    Direcțiile în care se poate mișcă blocul de pe tablă
-}

data Directions = North | South | West | East
    deriving (Show, Eq, Ord)

{-
    *** TODO ***

    Tip de date care va reprezenta plăcile care alcătuiesc harta și switch-urile
-}

data Cell = MakeCell Char

instance Show Cell where
    show (MakeCell a) = [a]

instance Eq Cell where
    (MakeCell a1) == (MakeCell a2) = a1 == a2

instance Ord Cell where
    compare = undefined

{-
    *** TODO ***

    Tip de date pentru reprezentarea nivelului curent
-}

data Level = MakeLevel {
    matrix :: A.Array Position Cell,
    blockPos :: (Position, Position),
    switchPos :: [(Position, [Position])],
    status :: Int,
    switchIsActive :: [(Position, Bool)]

} deriving (Eq, Ord)

{-
    *** Opțional *** 
  
    Dacă aveți nevoie de o funcționalitate particulară, 
    instantiati explicit clasele Eq și Ord pentru Level. 
    În cazul acesta, eliminați deriving (Eq, Ord) din Level. 
-}

 -- instance Eq Level where
 --    (==) = undefined

 -- instance Ord Level where
 --    compare = undefined

{-
    *** TODO ***

    Instantiati Level pe Show. 

    Atenție! String-ul returnat va fi urmat și precedat de un rând nou. 
    În cazul în care jocul este câștigat, la sfârșitul stringului se va mai
    concatena mesajul "Congrats! You won!\n". 
    În cazul în care jocul este pierdut, se va mai concatena "Game Over\n". 
-}
updateMatrix :: A.Array Position Cell -> (Position, Position) -> A.Array Position Cell
updateMatrix matrixAux blockPosition = 
    if (snd blockPosition == (-1, -1))
        then (matrixAux A.// [(fst blockPosition, MakeCell block)])
        else
            (matrixAux A.// [(fst blockPosition, MakeCell block), (snd blockPosition, MakeCell block)])

instance Show Level where
    show (MakeLevel {matrix = m, blockPos = b, switchPos = s, status = ok, switchIsActive = active}) = 
        "\n" ++ unlines [intercalate "" [show ( (A.!) (updateMatrix m b) (x, y)) | y <- [0 .. snd(snd((A.bounds) m))]] | x <- [0 .. fst(snd((A.bounds) m))]] ++ case ok of
            1    -> "Congrats! You won!\n"
            (-1)   -> "Game Over\n"
            _    -> ""

{-
    *** TODO ***

    Primește coordonatele colțului din dreapta jos a hârtii și poziția inițială a blocului.
    Întoarce un obiect de tip Level gol.
    Implicit, colțul din stânga sus este (0, 0).
-}

generate :: Position -> A.Array Position Cell
generate corner = A.array ((0,0), corner) cells
    where
        cells = fmap (\pos -> (pos, (MakeCell emptySpace))) [(i,j) | j <- [0..(snd corner)], i <- [0..(fst corner)]]

emptyLevel :: Position -> Position -> Level
emptyLevel posFinal posBlock = MakeLevel matrixx  (posBlock, (-1, -1)) [] 0 []
    where
        matrixx = generate posFinal
{-
    *** TODO ***

    Adaugă o celulă de tip Tile în nivelul curent.
    Parametrul char descrie tipul de tile adăugat: 
        'H' pentru tile hard 
        'S' pentru tile soft 
        'W' pentru winning tile 
-}
parseChar :: Char -> Cell
parseChar character = 
    if (character == 'S')
        then MakeCell softTile
        else if (character == 'H')
            then MakeCell hardTile
            else if (character == 'W')
                then MakeCell winningTile 
                else MakeCell emptySpace

addTile :: Char -> Position -> Level -> Level
addTile tileType tilePos (MakeLevel p1 p2 p3 p4 p5) = MakeLevel (newMatrix A.// [(tilePos, parseChar tileType)]) p2 p3 p4 p5
    where
        newMatrix = p1

{-
    *** TODO ***

    Adaugă o celulă de tip Swtich în nivelul curent.
    Va primi poziția acestuia și o listă de Position
    ce vor desemna pozițiile în care vor apărea sau 
    dispărea Hard Cells în momentul activării/dezactivării
    switch-ului.
-}

addSwitch :: Position -> [Position] -> Level -> Level
addSwitch switchPosition tilesActive (MakeLevel p1 p2 p3 p4 p5) = MakeLevel (newMatrix A.// [(switchPosition, MakeCell switch)]) p2 switchChange p4 activateSwitch
    where
        switchChange = [(switchPosition, tilesActive)] ++ p3
        newMatrix = p1
        activateSwitch = [(switchPosition, False)] ++ p5 

{-
    === MOVEMENT ===
-}

{-
    *** TODO ***

    Activate va verifica dacă mutarea blocului va activa o mecanică specifică. 
    În funcție de mecanica activată, vor avea loc modificări pe hartă. 
-}
getSwitchTiles :: Position -> [(Position, [Position])] -> [Position]
getSwitchTiles pos listSwitches = 
    if (pos == fst (head listSwitches))
        then snd (head listSwitches)
        else getSwitchTiles pos (tail listSwitches)

checkSwitch :: Position -> [(Position, Bool)] -> Bool
checkSwitch pos vec = 
    if (pos == fst (head vec))
        then snd (head vec)
        else checkSwitch pos (tail vec)

switchTheSwitch :: Position -> [(Position, Bool)] -> [(Position, Bool)]
switchTheSwitch pos vec = if (vec == [])
    then []
    else 
        if (fst (head vec) == pos && snd (head vec) == True)
            then [(pos, False)] ++ switchTheSwitch pos (tail vec)
            else if (fst (head vec) == pos && snd (head vec) == False)
                then [(pos, True)] ++ switchTheSwitch pos (tail vec)
                else [(fst (head vec), snd (head vec))] ++ switchTheSwitch pos (tail vec)

getSwitchVector :: Position -> [(Position, [Position])] -> [Position]
getSwitchVector position vec = if (fst (head vec) == position)
    then snd (head vec)
    else getSwitchVector position (tail vec)

turnOnSwitches :: [Position] ->[(Position, Cell)]
turnOnSwitches vec = if (vec == [])
    then []
    else
        [(head vec, MakeCell hardTile)] ++ turnOnSwitches (tail vec) 

turnOffSwitches :: [Position] ->[(Position, Cell)]
turnOffSwitches vec = if (vec == [])
    then []
    else
        [(head vec, MakeCell emptySpace)] ++ turnOffSwitches (tail vec) 
        

activate :: Position -> Level -> Level
activate currPos (MakeLevel p1 p2 p3 p4 p5) =
        if (p4 == -1)
            then MakeLevel p1 p2 p3 p4 p5
                else
                    if ((A.!) p1 (currPos) == (MakeCell emptySpace) || ((A.!) p1 (currPos) == (MakeCell softTile) && snd p2 == (-1, -1)))
                then
                    MakeLevel p1 p2 p3 (-1) p5
                else
                    if ((A.!) p1 (currPos) == MakeCell winningTile && snd p2 == (-1, -1))
                        then
                            MakeLevel p1 p2 p3 1 p5
                    else
                        if ((A.!) p1 (currPos) == MakeCell switch && checkSwitch (currPos) p5 == False)
                            then
                                MakeLevel (p1 A.// turnOnSwitches (getSwitchVector (currPos) p3)) p2 p3 p4 (switchTheSwitch (currPos) p5)
                            else
                                if ((A.!) p1 (currPos) == MakeCell switch && checkSwitch (currPos) p5 == True)
                                    then
                                        MakeLevel (p1 A.// turnOffSwitches (getSwitchVector (currPos) p3)) p2 p3 p4 (switchTheSwitch (currPos) p5)
                                    else
                                        MakeLevel p1 p2 p3 p4 p5

{-
    *** TODO ***

    Mișcarea blocului în una din cele 4 direcții 
    Hint: Dacă jocul este deja câștigat sau pierdut, puteți lăsa nivelul neschimbat.
-}

move :: Directions -> Level -> Level
move direction (MakeLevel p1 p2 p3 p4 p5) = if (p4 == -1 || p4 == 1)
    then MakeLevel p1 p2 p3 p4 p5
    else
        if (snd p2 == (-1, -1))
            then
                if (direction == North)
                    then activate (fst(fst p2) - 1, snd(fst p2)) (activate (fst(fst p2) - 2, snd(fst p2)) (MakeLevel p1 ((fst(fst p2) - 1, snd(fst p2)), (fst(fst p2) - 2, snd(fst p2))) p3 p4 p5))
                    else
                        if (direction == East)
                            then activate (fst(fst p2), snd(fst p2) + 1) (activate (fst(fst p2), snd(fst p2) + 2) (MakeLevel p1 ((fst(fst p2), snd(fst p2) + 1), (fst(fst p2), snd(fst p2) + 2)) p3 p4 p5))
                            else
                                if (direction == West)
                                    then activate (fst(fst p2), snd(fst p2) - 1) (activate (fst(fst p2), snd(fst p2) - 2) (MakeLevel p1 ((fst(fst p2), snd(fst p2) - 1), (fst(fst p2), snd(fst p2) - 2)) p3 p4 p5))
                                    else
                                        if (direction == South)
                                            then activate (fst(fst p2) + 1, snd(fst p2)) (activate (fst(fst p2) + 2, snd(fst p2)) (MakeLevel p1 ((fst(fst p2) + 1, snd(fst p2)), (fst(fst p2) + 2, snd(fst p2))) p3 p4 p5))
                                            else
                                                MakeLevel p1 p2 p3 p4 p5
            else
                if (direction == North)
                    then if ((fst(fst p2)) > (fst(snd p2))) -- cubul se afla in pozitita jos-sus
                            then activate (fst(snd p2) - 1, snd(snd p2)) (MakeLevel p1 ((fst(snd p2) - 1, snd(snd p2)), (-1, -1)) p3 p4 p5)
                            else if ((fst(fst p2)) < (fst(snd p2))) -- cubul se afla in pozitita sus-jos
                                then activate (fst(fst p2) - 1, snd(fst p2)) (MakeLevel p1 ((fst(fst p2) - 1, snd(fst p2)), (-1, -1)) p3 p4 p5)
                                else if ((snd(fst p2)) < (snd(snd p2))) -- cubul se afla in pozitita stanga-dreapta
                                    then activate (fst(fst p2) - 1, snd(fst p2)) (activate (fst(snd p2) - 1, snd(snd p2)) (MakeLevel p1 ((fst(fst p2) - 1, snd(fst p2)), (fst(snd p2) - 1, snd(snd p2))) p3 p4 p5))
                                    else if ((snd(fst p2)) > (snd(snd p2))) -- -- cubul se afla in pozitita dreapta-stanga
                                        then activate (fst(fst p2) - 1, snd(fst p2)) (activate (fst(snd p2) - 1, snd(snd p2)) (MakeLevel p1 ((fst(fst p2) - 1, snd(fst p2)), (fst(snd p2) - 1, snd(snd p2))) p3 p4 p5))
                                        else MakeLevel p1 p2 p3 p4 p5
                    else
                        if (direction == East)
                            then if ((fst(fst p2)) > (fst(snd p2))) -- cubul se afla in pozitita jos-sus
                                    then activate (fst(fst p2), snd(fst p2) + 1) (activate (fst(snd p2), snd(snd p2) + 1) (MakeLevel p1 ((fst(fst p2), snd(fst p2) + 1), (fst(snd p2), snd(snd p2) + 1)) p3 p4 p5))
                                    else if ((fst(fst p2)) < (fst(snd p2))) -- cubul se afla in pozitita sus-jos
                                        then activate (fst(fst p2), snd(fst p2) + 1) (activate (fst(snd p2), snd(snd p2) + 1) (MakeLevel p1 ((fst(fst p2), snd(fst p2) + 1), (fst(snd p2), snd(snd p2) + 1)) p3 p4 p5))
                                        else if ((snd(fst p2)) < (snd(snd p2))) -- cubul se afla in pozitita stanga-dreapta
                                            then activate (fst(fst p2), snd(snd p2) + 1) (MakeLevel p1 ((fst(fst p2), snd(snd p2) + 1), (-1, -1)) p3 p4 p5)
                                            else if ((snd(fst p2)) > (snd(snd p2))) -- -- cubul se afla in pozitita dreapta-stanga
                                                then activate (fst(fst p2), snd(fst p2) + 1) (MakeLevel p1 ((fst(fst p2), snd(fst p2) + 1), (-1, -1)) p3 p4 p5)
                                                else MakeLevel p1 p2 p3 p4 p5
                            else
                                if (direction == West)
                                    then if ((fst(fst p2)) > (fst(snd p2))) -- cubul se afla in pozitita jos-sus
                                        then activate (fst(fst p2), snd(fst p2) - 1) (activate (fst(snd p2), snd(snd p2) - 1) (MakeLevel p1 ((fst(fst p2), snd(fst p2) - 1), (fst(snd p2), snd(snd p2) - 1)) p3 p4 p5))
                                        else if ((fst(fst p2)) < (fst(snd p2))) -- cubul se afla in pozitita sus-jos
                                            then activate (fst(fst p2), snd(fst p2) - 1) (activate (fst(snd p2), snd(snd p2) - 1) (MakeLevel p1 ((fst(fst p2), snd(fst p2) - 1), (fst(snd p2), snd(snd p2) - 1)) p3 p4 p5))
                                            else if ((snd(fst p2)) < (snd(snd p2))) -- cubul se afla in pozitita stanga-dreapta
                                                then activate (fst(fst p2), snd(fst p2) - 1) (MakeLevel p1 ((fst(fst p2), snd(fst p2) - 1), (-1, -1)) p3 p4 p5)
                                                else if ((snd(fst p2)) > (snd(snd p2))) -- -- cubul se afla in pozitita dreapta-stanga
                                                    then activate (fst(fst p2), snd(snd p2) - 1) (MakeLevel p1 ((fst(fst p2), snd(snd p2) - 1), (-1, -1)) p3 p4 p5)
                                                    else MakeLevel p1 p2 p3 p4 p5
                                    else
                                        if (direction == South)
                                            then if ((fst(fst p2)) > (fst(snd p2))) -- cubul se afla in pozitita jos-sus
                                                then activate (fst(fst p2) + 1, snd(fst p2)) (MakeLevel p1 ((fst(fst p2) + 1, snd(fst p2)), (-1, -1)) p3 p4 p5)
                                                else if ((fst(fst p2)) < (fst(snd p2))) -- cubul se afla in pozitita sus-jos
                                                    then activate (fst(snd p2) + 1, snd(fst p2)) (MakeLevel p1 ((fst(snd p2) + 1, snd(fst p2)), (-1, -1)) p3 p4 p5)
                                                    else if ((snd(fst p2)) < (snd(snd p2))) -- cubul se afla in pozitita stanga-dreapta
                                                        then activate (fst(fst p2) + 1, snd(fst p2)) (activate (fst(snd p2) + 1, snd(snd p2)) (MakeLevel p1 ((fst(fst p2) + 1, snd(fst p2)), (fst(snd p2) + 1, snd(snd p2))) p3 p4 p5))
                                                        else if ((snd(fst p2)) > (snd(snd p2))) -- -- cubul se afla in pozitita dreapta-stanga
                                                            then activate (fst(fst p2) + 1, snd(fst p2)) (activate (fst(snd p2) + 1, snd(snd p2)) (MakeLevel p1 ((fst(fst p2) + 1, snd(fst p2)), (fst(snd p2) + 1, snd(snd p2))) p3 p4 p5))
                                                            else MakeLevel p1 p2 p3 p4 p5
                                            else
                                                MakeLevel p1 p2 p3 p4 p5
        

{-
    *** TODO ***

    Va returna True dacă jocul nu este nici câștigat, nici pierdut.
    Este folosită în cadrul Interactive.
-}

continueGame :: Level -> Bool
continueGame (MakeLevel _ _ _ p4 _) = if (p4 == 0)
    then True
    else False

{-
    *** TODO ***

    Instanțiați clasa `ProblemState` pentru jocul nostru. 
  
    Hint: Un level câștigat nu are succesori! 
    De asemenea, puteți ignora succesorii care 
    duc la pierderea unui level.
-}
getStatus :: Level -> Int
getStatus (MakeLevel p1 p2 p3 p4 p5) = p4

instance ProblemState Level Directions where
    successors level
        | isGoal level == True = []
        | otherwise = filter (\state -> (status (snd state)) /= -1) directions
            where
                directions = [(North, moveNorth), (East, moveEast), (West, moveWest), (South, moveSouth)]
                moveNorth = move North level
                moveEast = move East level
                moveWest = move West level
                moveSouth = move South level


    isGoal level
        | (status level) == 1 = True
        | otherwise  = False

    -- Doar petru BONUS
    -- heuristic = undefined
