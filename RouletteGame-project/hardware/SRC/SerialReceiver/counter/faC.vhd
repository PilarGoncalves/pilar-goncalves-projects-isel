library ieee;
use ieee.std_logic_1164.all;

entity faC is
	port(
   A, B, Ci: in std_logic;
   S, Co: out std_logic
   );
end faC;

architecture structural of faC is

component haC is
    port(
    A, B: in std_logic;
    S, Co: out std_logic
    );
end component;

signal addAB: std_logic;
signal carry: std_logic_vector(1 downto 0);

begin

U1: haC port map (A => A, B => B, S => addAB, Co => carry(0));
U2: haC port map (A => addAB, B => Ci, S => S, Co => carry(1));

Co <= carry(0) or carry(1);


end structural;