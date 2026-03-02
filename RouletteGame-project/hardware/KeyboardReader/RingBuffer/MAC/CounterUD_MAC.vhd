library ieee;
use ieee.std_logic_1164.all;

entity CounterUD_MAC is 
    port (
        CE, CLK, RESET, UP_DOWN : in std_logic;
        Q : out std_logic_vector(4 downto 0)
    );
end CounterUD_MAC;

architecture structural of CounterUD_MAC is

component Adder5_MAC is 
	port(
   A, B : in std_logic_vector(4 downto 0);
   Ci : in std_logic;
   Co : out std_logic;
   S : out std_logic_vector(4 downto 0)
   );
end component;

component Reg5_MAC is
	port( 
	CLK : in std_logic;
	RESET : in std_logic;
	D : in std_logic_vector(4 downto 0);
	EN : in std_logic;
	Q : out std_logic_vector(4 downto 0)
   );
end component;

component MUX5_2_1 is
	port(
	A, B : in std_logic_vector(4 downto 0);
	S : in std_logic;
	Y : out std_logic_vector(4 downto 0)
	);
end component;

signal Adder_Reg, Reg_exit, B_mux : std_logic_vector(4 downto 0);

begin
U0: MUX5_2_1 port map(
        A => "00001",
        B => "11111",
        S => UP_DOWN,
        Y => B_mux
		  );

U1: Adder5_MAC port map(
	A => Reg_exit,
	B => B_mux,
	Ci => '0',
	Co => open,
	S => Adder_Reg
	);

U2: Reg5_MAC port map(
	CLK => CLK,
	RESET => RESET,
	D => Adder_Reg,
	EN => CE,
	Q => Reg_exit
   );

Q <= Reg_exit;

end structural;