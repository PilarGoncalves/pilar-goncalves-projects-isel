library ieee;
use ieee.std_logic_1164.all;

entity SerialReceiver_SRC is 
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
end SerialReceiver_SRC; 	
	
architecture structural of SerialReceiver_SRC is

component SerialControl_SRC is 
	port( 
	RESET : in std_logic;
	CLK : in std_logic;
	enRx : in std_logic;
	accept : in std_logic;
   pFlag : in std_logic;
   dFlag : in std_logic;
	RXerror : in std_logic;
   wr : out std_logic;
	init : out std_logic;
	DXval : out std_logic
	);
end component;

component ParityCheck_SRC is 
	port (
	data: in std_logic;
	init: in std_logic;
	CLK: in std_logic;
	err: out std_logic
	);
end component;

component ShiftRegister_SRC is 
	port(
	data: in std_logic;
	enableShift: in std_logic;
	CLK: in std_logic;
	D: out std_logic_vector(7 downto 0)
	);
end component;

component counterSR_SRC is 
	port (
	CLK, clr: in std_logic; 
	Q: out std_logic_vector (3 downto 0) 
	);
end component;

component comparatorSR_SRC is 
	port (
	A: in std_logic_vector (3 downto 0); 
	B: in std_logic_vector (3 downto 0); 
	TC: out std_logic
	); 
end component;

signal SR_exit : std_logic_vector(7 downto 0);  
signal wr_eS : std_logic; 
signal counter_exit : std_logic_vector(3 downto 0); 
signal init_exit : std_logic; 
signal DXval_exit : std_logic; 
signal err_RX : std_logic; 
signal cinco_dF : std_logic; 
signal seis_pF : std_logic; 

begin
U0: SerialControl_SRC port map ( RESET => RESET, CLK => MCLK, enRx => not SS, accept => accept, 
pFlag => seis_pF, dFlag => cinco_dF, RXerror => err_RX, wr => wr_eS, init => init_exit, 
DXval => DXval_exit );

U1: ParityCheck_SRC port map ( data => SDX, init => init_exit, CLK => SCLK, err => err_RX );

U2: ShiftRegister_SRC port map ( data => SDX, enableShift => wr_eS, CLK => SCLK, D => SR_exit );

U3: counterSR_SRC port map ( CLK => SCLK, clr => init_exit, Q => counter_exit);

U4: comparatorSR_SRC port map ( A => counter_exit, B => "1000", TC => cinco_dF );

U5: comparatorSR_SRC port map ( A => counter_exit, B => "1001", TC => seis_pF );

DXval <= DXval_exit;
D <= SR_exit;

end structural;



