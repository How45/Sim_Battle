import unittest
import json
import poke_api as poke
from icecream import ic
from pokebase import pokemon, APIResource

class PokemonTest(unittest.TestCase):

    def setUp(self) -> None:
        self.name : str = 'pikachu'
        self.raw_pokemon_data = pokemon(self.name)
        self.pikachu = poke.Pokemon_info()

    def api_retrieve(self) -> None:
        self.pikachu.normalise(self.raw_pokemon_data)

        ic(self.pikachu.pokemon_info)

    def cal_stats(self) -> None:
        self.pikachu.normalise(self.raw_pokemon_data)

        self.pikachu.stat_calculation(81)
        ic(self.pikachu.lvl_base_stat)
        ic(self.pikachu.random_values)

    def moves_retrieve(self) -> None:
        self.pikachu.normalise(self.raw_pokemon_data)

        self.pikachu.pokemon_move('growl')
        self.pikachu.pokemon_move('thunder-shock')

        ic(self.pikachu.move_set)

    def export_json(self) -> None:
        poke_name: str = 'pikachu'
        random_pokemon: APIResource = pokemon(poke_name)
        radom_poke = poke.Pokemon_info()

        radom_poke.normalise(random_pokemon)
        radom_poke.stat_calculation(9)

        moves : list[str] = ['thunder-shock', 'thunder-wave', 'growl']

        for m in moves:
            radom_poke.pokemon_move(m)

        with open(f'{poke_name}.json', 'w', encoding='utf-8') as f:
            json.dump(radom_poke.__dict__, f, indent=4)



if __name__ == '__main__':
    unittest.main()
