#include "hlt/game.hpp"
#include "hlt/constants.hpp"
#include "hlt/log.hpp"
#include "hlt/position.hpp"

#include <random>
#include <ctime>
#include <map>
#include <algorithm>

using namespace std;
using namespace hlt;


/**
 * Avoid collisions with other ships by checking if the surroundings are occupied or if
 * other ships are already going to be moved in that place. Also keep the vector with
 * the directions ordered.
 *
 * @param all_positions
 *      A vector with the surroundings of the current_ship and the position
 *      it is in.
 * @param directions
 *      The corresponding directions for the all_positions vector.
 * @param other_ships
 *      A vector with all the ships of the player.
 * @param marked_positions
 *      A vector with the already used positions of the current turn.
 * @param current_ship
 *      The ship which this function tries to protect.
 * @return
 *      The function returns nothing but it will modify the all_positions and
 *      directions vectors.
 */
void avoid_collisions(std::vector<Position> & all_positions,
        std::vector<Direction> & directions,
        std::unordered_map<EntityId, std::shared_ptr<Ship>> other_ships,
        std::vector<Position> marked_positions,
        shared_ptr<Ship> current_ship) {

    // Don't clash with other ships
    for (const auto& other_ship : other_ships) {
        if (current_ship->id == other_ship.second->id) {
            continue;
        }

        auto index = std::find(all_positions.begin(), all_positions.end(), other_ship.second->position);

        if (index != all_positions.end()) {
            all_positions.erase(index);
            directions.erase(directions.begin() + std::distance(all_positions.begin(), index));
        }
    }

    // Don't clash with the new positions of other ships
    for (const auto& marked_position : marked_positions) {
        auto index = std::find(all_positions.begin(), all_positions.end(), marked_position);

        if (index != all_positions.end()) {
            all_positions.erase(index);
            directions.erase(directions.begin() + std::distance(all_positions.begin(), index));
        }
    }
}

/*
 * Then map the positions with their directions and search for the
 * cell with the most halite available.
 */
/**
 * Use this function to find the cell with the most halite in the all_positions
 * vector, while also keeping track of the directions used to get to that
 * position.
 * @param all_positions
 *      The vector with all the positions we are looking for.
 * @param directions
 *      The corresponding directions of the all_positions vector.
 * @param game_map
 *      The Game Map of the Game
 * @return
 *      Returns a pair with the cell that contains the most halite(it's
 *      position and the corresponding direction).
 */
pair<Position, Direction> select_richest_cell(std::vector<Position> all_positions,
                                        std::vector<Direction> directions,
                                        unique_ptr<GameMap>& game_map) {

    /*
     * Map the positions with their directions and search for the cell with the
     * most halite available.
     */
    std::map<Position, Direction> choices;

    for (unsigned int i = 0; i < all_positions.size(); i++) {
        choices.insert(pair<Position, Direction>(all_positions[i], directions[i]));
    }

    auto pr = std::max_element
            (
                    std::begin(choices), std::end(choices),
                    [&game_map] (const pair<Position, Direction> & p1, const pair<Position, Direction> & p2) {
                        return game_map->at(p1.first)->halite < game_map->at(p2.first)->halite;
                    }
            );

    return std::make_pair(pr->first, pr->second);
}

/**
 * This function will direct a ship to the shipyard while avoiding collisions.
 * Future implementation: select the closest dropoff and go to that one.
 * @param ship
 *      The current ship.
 * @param game
 *      The game itself.
 * @param marked_positions
 *      A vector with the already used positions of the current turn.
 * @return
 *      Returns a pair with the cell that represents the best route to go to
 *      the shipyard.
 */
pair<Position, Direction> go_to_deposit(const std::shared_ptr<Ship> &ship,
                                        Game *game,
                                        std::vector<Position> marked_positions) {
    /*
     * Use naive_navigate to find the best route to go to the shipyard and use
     * directional_offset to find the corresponding position.
     */
    Direction direction = game->game_map->naive_navigate(ship, game->me->shipyard->position);
    Position position = ship->position.directional_offset(direction);

    /*
     * If the new cell is free, then return it. Else, the ship will not move.
     */
    if (std::find(marked_positions.begin(), marked_positions.end(), position)
        == marked_positions.end()) {
        return std::make_pair(position, direction);
    } else {
        return std::make_pair(ship->position, Direction::STILL);
    }
}

int main(int argc, char* argv[]) {
    unsigned int rng_seed;
    if (argc > 1) {
        rng_seed = static_cast<unsigned int>(stoul(argv[1]));
    } else {
        rng_seed = static_cast<unsigned int>(time(nullptr));
    }
    mt19937 rng(rng_seed);

    Game game;

    // Definition of constants.
    const float ENOUGHHALITEFACTOR = 1.2;
    const int TURNSFORBUILDINGSHIPS = 100;
    const float GOBACKCOLLECTINGFACTOR = 2.2;
    const float NOTENOUGHHALITEFACTOR = 10;

    // At this point "game" variable is populated with initial map data.
    // This is a good place to do computationally expensive start-up pre-processing.
    // As soon as you call "ready" function below, the 2 second per turn timer will start.
    game.ready("MyCppBot");

    log::log("Successfully created bot! My Player ID is " + to_string(game.my_id) + ". Bot rng seed is " + to_string(rng_seed) + ".");

    for (;;) {

        game.update_frame();
        shared_ptr<Player> me = game.me;
        unique_ptr<GameMap>& game_map = game.game_map;

        vector<Command> command_queue;

        std::vector<Position> marked_positions;

        for (const auto& ship_iterator : me->ships) {
            shared_ptr<Ship> ship = ship_iterator.second;
            /*
             * Get the surrounding positions of the ship and the current
             * position and create a vector with all possible positions and
             * another vector with the corresponding directions.
            */

            std::array<Position, 4> surroundings = ship->position.get_surrounding_cardinals();
            std::vector<Position> all_positions(surroundings.begin(), surroundings.end());
            all_positions.push_back(ship->position);

            std::vector<Direction> directions{Direction::NORTH, Direction::SOUTH, Direction::EAST,
                                                   Direction ::WEST, Direction::STILL};

            /*
             * Delete the already used positions and directions in order to
             * avoid collisions.
             */
            avoid_collisions(all_positions, directions, me->ships, marked_positions, ship);



            pair<Position, Direction> choice;

            /*
             * Currently not working: trying to set a flag when the ship should
             * go back to the shipyard and reset it when the ship's halite gets
             * too low.
             */
            if (ship->halite < constants::MAX_HALITE / GOBACKCOLLECTINGFACTOR) {
                ship->go_to_deposit = false;
            }

            /*
             * If the ship has enough halite, then go to the shipyard. Else, if
             * the current cell has few halite, then move to the cell with the
             * most halite. Else, stay in the current position.
             */
            if (ship->halite >= constants::MAX_HALITE / ENOUGHHALITEFACTOR
                    || ship->go_to_deposit) {
                choice = go_to_deposit(ship_iterator.second, &game, marked_positions);
                ship->go_to_deposit = true;
            } else if (game_map->at(ship)->halite < constants::MAX_HALITE / NOTENOUGHHALITEFACTOR){
                choice  = select_richest_cell(all_positions, directions, game_map);
            } else {
                choice = std::make_pair(ship->position, Direction::STILL);
            }

            command_queue.push_back(ship->move(choice.second));
            marked_positions.push_back(choice.first);
        }

        // Create ships for the first TURNSFORBUILDINGSHIPS turns.
        if (
            game.turn_number <= TURNSFORBUILDINGSHIPS &&
            me->halite >= constants::SHIP_COST &&
            !game_map->at(me->shipyard)->is_occupied()) {

            command_queue.push_back(me->shipyard->spawn());
        }

        if (!game.end_turn(command_queue)) {
            break;
        }
    }

    return 0;
}
