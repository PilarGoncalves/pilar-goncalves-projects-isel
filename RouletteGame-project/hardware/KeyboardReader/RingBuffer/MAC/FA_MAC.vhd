library ieee;
use ieee.std_logic_1164.all;

entity FA_MAC is
	port(
   A, B, Ci: in std_logic;
   S, Co: out std_logic
   );
end FA_MAC;

architecture structural of FA_MAC is

component HA_MAC is
    port(
    A, B: in std_logic;
    S, Co: out std_logic
    );
end component;

signal addAB: std_logic;
signal carry: std_logic_vector(1 downto 0);

begin

U1: HA_MAC port map (A => A, B => B, S => addAB, Co => carry(0));
U2: HA_MAC port map (A => addAB, B => Ci, S => S, Co => carry(1));

Co <= carry(0) or carry(1);


end structural;