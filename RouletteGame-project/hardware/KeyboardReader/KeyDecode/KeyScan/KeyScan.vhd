library ieee;
use ieee.std_logic_1164.all;

entity KeyScan is 
	port( 
	Kscan : in std_logic;
	RESET : in std_logic;
	CLK : in std_logic; 
	inMUX : in std_logic_vector (3 downto 0); 
	K : out std_logic_vector (3 downto 0);
	Kpress : out std_logic;
	outDEC : out std_logic_vector(3 downto 0) 
	);
end KeyScan; 

architecture structural of KeyScan is

component Counter is
	port (
	CE,CLK,RESET : in std_logic; 
	Q: out std_logic_vector (3 downto 0) 
	);
end component;

component DEC2_4 is
	port(
	S0, S1: in std_logic;
	A, B, C, D: out std_logic
	);
end component;

component MUX4_1 is
	port(
    A, B, C, D : in std_logic;
    S0, S1 : in std_logic;
    Y : out std_logic
    );
end component;

signal exitMUX: std_logic;
signal q: std_logic_vector (3 downto 0);

begin
 
U0: Counter port map ( CE => Kscan , CLK => CLK , RESET => RESET , Q => q );
U1: DEC2_4 port map ( S1 => q(3), S0 => q(2), A => outDEC(0), B => outDEC(1), C => outDEC(2), 
D => outDEC(3) );
U2: MUX4_1 port map ( A => inMUX(0) , B => inMux(1), C => inMUx(2) , D => inMux(3), S1 => q(1), S0 => q(0), Y => exitMUX );
K <= q;
Kpress <= exitMUX;

end structural;