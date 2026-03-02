library ieee;
use ieee.std_logic_1164.all;

entity SRC is 
	port(
	SS: in std_logic;
	SDX: in std_logic;
	SCLK:in std_logic;
	MCLK: in std_logic;
	RESET: in std_logic;
	WrD:out std_logic;
	Dout: out std_logic_vector(7 downto 0)
	);
end SRC;

architecture structural of SRC is 

component SerialReceiver_SRC is 
	port( 
	SS : in std_logic;
	MCLK : in std_logic;
	SDX : in std_logic;
	RESET : in std_logic;
	SCLK : in std_logic;
	accept : in std_logic;
	D : out std_logic_vector(7 downto 0);
	DXval : out std_logic
	);
end component;

component Roulette_Dispatcher is 
	port(
	Dval : in std_logic;
	CLK : in std_logic;
	RESET : in std_logic;
	done : out std_logic;
	Din: in std_logic_vector(7 downto 0);
	Dout: out std_logic_vector(7 downto 0);
	WrD : out std_logic
	);
end component; 

signal done_accept : std_logic;
signal Dsignal : std_logic_vector(7 downto 0);
signal Dval_signal : std_logic; 

begin 
U0: SerialReceiver_SRC port map ( MCLK => MCLK, RESET => RESET, SDX => SDX, SCLK => SCLK, 
SS => SS, accept => done_accept, D => Dsignal, DXval => Dval_signal );

U1: Roulette_Dispatcher port map ( Dval => Dval_signal, Din => Dsignal, RESET => RESET, 
CLK => MCLK, done => done_accept, WrD => WrD, Dout => Dout );

end structural;