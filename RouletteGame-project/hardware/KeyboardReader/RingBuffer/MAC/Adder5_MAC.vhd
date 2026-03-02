library ieee;
use ieee.std_logic_1164.all;

entity Adder5_MAC is 
	port(
	A, B: in std_logic_vector(4 downto 0);
	Ci: in std_logic;
	S: out std_logic_vector(4 downto 0);
	Co: out std_logic
	);
end Adder5_MAC;

architecture structural of Adder5_MAC is

component FA_MAC is 
	port(
	A, B, Ci : in std_logic;
	S, Co: out std_logic
	);
end component;

signal carry: std_logic_vector(4 downto 1);

begin 

U0: FA_MAC port map (A => A(0), B => B(0), Ci => Ci, S => S(0), Co => carry(1));
U1: FA_MAC port map (A => A(1), B => B(1), Ci => carry(1), S => S(1), Co => carry(2));
U2: FA_MAC port map (A => A(2), B => B(2), Ci => carry(2), S => S(2), Co => carry(3));
U3: FA_MAC port map (A => A(3), B => B(3), Ci => carry(3), S => S(3), Co => carry(4));
U4: FA_MAC port map (A => A(4), B => B(4), Ci => carry(4), S => S(4), Co => Co);

end structural;